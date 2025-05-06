package uz.lb.fxchatserver.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.FileUrlResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import uz.lb.fxchatserver.dto.ResultDTO;
import uz.lb.fxchatserver.service.implement.AttachmentService;

@Slf4j
@RestController
@RequestMapping("/server/file")
@RequiredArgsConstructor
public class AttachmentController {

    private final AttachmentService attachmentService;

    @GetMapping("/get-all")
//    @PreAuthorize("hasAnyRole('ROLE_USER')")
    public ResponseEntity<ResultDTO> getAllAttachment() {
        return attachmentService.getAllAttachment();
    }

    @PostMapping("/save")
//    @PreAuthorize("hasAnyRole('ROLE_USER')")
    public ResponseEntity<ResultDTO> saveAttachment(@Valid @RequestParam(value = "file") MultipartFile multipartFile) {
        return attachmentService.saveAttachment(multipartFile);
    }

    @GetMapping("/preview/{hashId}")
    public ResponseEntity<FileUrlResource> preview(@PathVariable String hashId) {
        return attachmentService.preview(hashId);
    }

    @GetMapping("/download/{hashId}")
    public ResponseEntity<FileUrlResource> download(@PathVariable String hashId) {
        return attachmentService.download(hashId);
    }

    @DeleteMapping("/delete/{hashId}")
    public ResponseEntity<ResultDTO> delete(@PathVariable String hashId) {
        return attachmentService.removeAttachmentByHashId(hashId);
    }
}
