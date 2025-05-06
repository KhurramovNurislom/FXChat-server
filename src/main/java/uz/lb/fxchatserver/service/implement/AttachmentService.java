package uz.lb.fxchatserver.service.implement;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileUrlResource;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import uz.lb.fxchatserver.config.CustomUserDetails;
import uz.lb.fxchatserver.dto.ResultDTO;
import uz.lb.fxchatserver.entity.Attachment;
import uz.lb.fxchatserver.entity.User;
import uz.lb.fxchatserver.exception.ItemNotFoundException;
import uz.lb.fxchatserver.repository.AttachmentRepository;
import uz.lb.fxchatserver.service.IAttachmentService;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URLEncoder;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class AttachmentService implements IAttachmentService {

    private final AttachmentRepository attachmentRepository;
    private final UserService userService;

    @Value("${upload.folder}")
    private String uploadFolder;

    private final ResultDTO result = new ResultDTO();

    @Override
    public ResponseEntity<ResultDTO> getAllAttachment() {
        List<Attachment> attachments = attachmentRepository.findAll(Sort.by("createdAt"));
        if (attachments.isEmpty()) {
            log.error("AttachmentService.getAllAttachment => {}", "attachments does not exist");
            throw new ItemNotFoundException("attachment does not exist");
        }
        return ResponseEntity.status(HttpStatus.OK).body(result.success(attachments));
    }

    @Override
    public ResponseEntity<ResultDTO> saveAttachment(MultipartFile multipartFile) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails user = (CustomUserDetails) authentication.getPrincipal();

        Attachment attachment = new Attachment();
        attachment.setUser((User) userService.findUserById(user.getId()).getBody().getData());
        attachment.setContentType(multipartFile.getContentType());
        attachment.setFileName(multipartFile.getOriginalFilename().toLowerCase());
        attachment.setFileSize(multipartFile.getSize() / (1024f));
        attachment.setHashId(UUID.randomUUID().toString());
        attachment.setExtension(getExtension(multipartFile.getOriginalFilename().toLowerCase()));
        LocalDate date = LocalDate.now();
        String uploadPath = String.format("%s/%d/%d/%d/%s", uploadFolder, date.getYear(), date.getMonthValue(), date.getDayOfMonth(), attachment.getExtension());

        File file = new File(uploadPath);
        if (!file.exists()) {
            file.mkdirs();
        }

        attachment.setUploadPath(uploadPath);
        attachment.setLink(String.format("%s/%s.%s", file.getAbsolutePath(),
                attachment.getHashId(), attachment.getExtension()));
        try {
            multipartFile.transferTo(new File(attachment.getLink()));
        } catch (IOException e) {
            log.error("AttachmentService.saveAttachment => {}", e.getMessage());
            throw new RuntimeException(e);
        }
        attachmentRepository.save(attachment);
        return ResponseEntity.status(HttpStatus.OK).body(result.success(attachment));
    }

    @Override
    public ResponseEntity<FileUrlResource> preview(String hashId) {
        ResultDTO result = findAttachmentByHashId(hashId);
        if (result.isStatus()) {
            Attachment attachment = (Attachment) result.getData();
            try {
                return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, "inline; fileName="
                                + URLEncoder.encode(attachment.getFileName()))
                        .contentType(MediaType.parseMediaType(attachment.getContentType()))
                        .body(new FileUrlResource(attachment.getLink()));

            } catch (MalformedURLException e) {
                log.error("AttachmentController.preview.MalformedURLException => {}", e.getMessage());
                throw new RuntimeException(e.getMessage());
            }
        } else {
            log.error("AttachmentService.preview => {}", result.getMessage());
            throw new RuntimeException(result.getMessage());
        }
    }

    @Override
    public ResponseEntity<FileUrlResource> download(String hashId) {
        ResultDTO result = findAttachmentByHashId(hashId);
        if (result.isStatus()) {
            Attachment attachment = (Attachment) result.getData();
            try {
                return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, "attachment; fileName="
                                + URLEncoder.encode(attachment.getFileName()))
                        .contentType(MediaType.parseMediaType(attachment.getContentType()))
                        .body(new FileUrlResource(attachment.getLink()));

            } catch (MalformedURLException e) {
                log.error("AttachmentController.preview.MalformedURLException => {}", e.getMessage());
                throw new RuntimeException(e.getMessage());
            }
        } else {
            log.error("AttachmentService.download => {}", result.getMessage());
            throw new RuntimeException(result.getMessage());
        }
    }

    private String getExtension(String fileName) {
        return fileName.substring(fileName.lastIndexOf(".") + 1);
    }

    public ResultDTO findAttachmentByHashId(String hashId) {
        Attachment attachment = attachmentRepository.findByHashId(hashId);
        if (attachment == null) return result.error("hashId not found");
        else return result.success(attachment);
    }

    @Override
    public ResponseEntity<ResultDTO> removeAttachmentByHashId(String hashId) {

        /** bu yerda role admin bo'lmasa delete emas visible false qilish zarur */

        try {
            Attachment attachment = attachmentRepository.findByHashId(hashId);
            attachmentRepository.delete(attachment);
            new File(attachment.getLink()).delete();
            return ResponseEntity.status(HttpStatus.OK).body(new ResultDTO().success(result.success(attachment)));
        } catch (Exception e) {
            log.error("AttachmentService.removeAttachmentByHashId => {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResultDTO().error(e));
        }
    }
}
