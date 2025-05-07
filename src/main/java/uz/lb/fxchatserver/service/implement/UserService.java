package uz.lb.fxchatserver.service.implement;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import uz.lb.fxchatserver.config.CustomUserDetails;
import uz.lb.fxchatserver.crypto.SHA256;
import uz.lb.fxchatserver.dto.ResultDTO;
import uz.lb.fxchatserver.dto.auth.AuthRequestDTO;
import uz.lb.fxchatserver.dto.auth.AuthResponseDTO;
import uz.lb.fxchatserver.entity.User;
import uz.lb.fxchatserver.enums.AccountRoleEnums;
import uz.lb.fxchatserver.enums.GeneralStatus;
import uz.lb.fxchatserver.exception.AppBadRequestException;
import uz.lb.fxchatserver.exception.ItemNotFoundException;
import uz.lb.fxchatserver.payload.UserPayload;
import uz.lb.fxchatserver.repository.UserRepository;
import uz.lb.fxchatserver.service.IUserService;
import uz.lb.fxchatserver.util.JwtUtil;

import java.util.List;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class UserService implements IUserService {

    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final ResultDTO resultDTO = new ResultDTO();

    @Override
    public ResponseEntity<ResultDTO> authorization(AuthRequestDTO authRequestDTO) {
        try {

            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequestDTO.getLogin(), authRequestDTO.getPassword()));
            if (authentication.isAuthenticated()) {
                CustomUserDetails user = (CustomUserDetails) authentication.getPrincipal();
                AuthResponseDTO authResponseDTO = new AuthResponseDTO();

                authResponseDTO.setLogin(user.getUsername());
                authResponseDTO.setStatus(GeneralStatus.ACTIVE);
                authResponseDTO.setRole(user.getRole());
                authResponseDTO.setJwt(JwtUtil.encode(user.getUsername(), user.getRole()));
                return ResponseEntity.status(HttpStatus.OK).body(resultDTO.success(authResponseDTO));
            }
            return ResponseEntity.status(HttpStatus.OK).body(resultDTO.error("Login and password wrong or user not active"));
        } catch (BadCredentialsException e) {
            log.error("UserService.authorization(AuthRequestDTO authRequestDTO) => {}", e.getMessage());
            throw new UsernameNotFoundException("Login or password wrong");
        }

    }

    @Override
    public ResponseEntity<ResultDTO> saveUser(UserPayload userPayload) {

        User user = userRepository.findUserByLogin(userPayload.getLogin());
        if (user != null) {
            log.error("UserService.saveUser => {}", "there is an account with this login");
            throw new AppBadRequestException("there is an account with this login");
        }
        return ResponseEntity.status(HttpStatus.OK).body(resultDTO.success(userRepository.save(User.builder()
                .login(userPayload.getLogin())
                .password(SHA256.getSHA256Hash((userPayload.getPassword())))
                .role(AccountRoleEnums.ROLE_USER)
                .build())));
    }

    @Override
    public ResponseEntity<ResultDTO> getAllUsers() {
        List<User> users = userRepository.findAll(Sort.by("createdAt"));
        if (users.isEmpty()) {
            log.error("UserService.getAllUsers => {}", "account does not exist");
            throw new ItemNotFoundException("account does not exist");
        }
        return ResponseEntity.status(HttpStatus.OK).body(resultDTO.success(users));
    }

    @Override
    public ResponseEntity<ResultDTO> findUserById(Long id) {
        User user = userRepository.findById(id).orElse(new User());
        if (user.getLogin() == null) {
            log.error("UserService.findUserById => {}", "user with this id was not found");
            throw new ItemNotFoundException("user with this id was not found");
        }
        return ResponseEntity.status(HttpStatus.OK).body(resultDTO.success(user));
    }

    @Override
    public ResponseEntity<ResultDTO> findUserByLogin(String login) {
        User user = userRepository.findUserByLogin(login);
        if (user == null) {
            log.error("UserService.findUserByLogin => {}", "user with this login was not found");
            throw new ItemNotFoundException("user with this login was not found");
        }
        return ResponseEntity.status(HttpStatus.OK).body(resultDTO.success(user));
    }

    @Override
    public ResponseEntity<ResultDTO> findUsersByLogin(String login) {
        List<User> users = userRepository.findUsersByLogin(login);
        if (users.isEmpty()) {
            log.error("UserService.findUsersByLogin => {}", "user with this login was not found");
            throw new ItemNotFoundException("user with this login was not found");
        }
        return ResponseEntity.status(HttpStatus.OK).body(resultDTO.success(users));
    }

    @Override
    public ResponseEntity<ResultDTO> editUserById(Long id, UserPayload userPayload) {
        User user = userRepository.findById(id).orElseThrow(() -> {
            log.error("UserService.editUserById => {}", "user with this id was not found");
            return new ItemNotFoundException("user with this id was not found");
        });
        return getResultDTOResponseEntity(userPayload, user);
    }

    @Override
    public ResponseEntity<ResultDTO> editUserByLogin(String login, UserPayload userPayload) {
        User user = userRepository.findUserByLogin(login);
        if (user == null) {
            log.error("UserService.editUserByLogin => {}", "user with this login was not found");
            throw new ItemNotFoundException("user with this login was not found");
        }
        return getResultDTOResponseEntity(userPayload, user);
    }

    private ResponseEntity<ResultDTO> getResultDTOResponseEntity(UserPayload userPayload, User user) {
        user.setLogin(userPayload.getLogin());
        user.setPassword(SHA256.getSHA256Hash(userPayload.getPassword()));
        userRepository.save(user);
        return ResponseEntity.status(HttpStatus.OK).body(resultDTO.success(user));
    }

    @Override
    public ResponseEntity<ResultDTO> deleteUserById(Long id) {
        User user = userRepository.findById(id).orElse(new User());
        if (user.getLogin() == null) {
            log.error("UserService.deleteUserById => {}", "user with this id was not found");
            throw new ItemNotFoundException("user with this id was not found");
        }
        userRepository.deleteById(id);
        return ResponseEntity.status(HttpStatus.OK).body(resultDTO.success("user deleted successfully"));
    }

    @Override
    public ResponseEntity<ResultDTO> deleteUserByLogin(String login) {
        User user = userRepository.findUserByLogin(login);
        if (user == null) {
            log.error("UserService.deleteUserByLogin => {}", "user with this login was not found");
            throw new ItemNotFoundException("user with this login was not found");
        }
        userRepository.delete(user);
        return ResponseEntity.status(HttpStatus.OK).body(resultDTO.success("user deleted successfully"));
    }
}