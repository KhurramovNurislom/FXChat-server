package uz.lb.fxchatserver.service;


import org.springframework.http.ResponseEntity;
import uz.lb.fxchatserver.dto.ResultDTO;
import uz.lb.fxchatserver.dto.auth.AuthRequestDTO;
import uz.lb.fxchatserver.payload.UserPayload;

public interface IUserService {
    ResponseEntity<ResultDTO> authorization(AuthRequestDTO authRequestDTO);

    ResponseEntity<ResultDTO> saveUser(UserPayload userPayload);

    ResponseEntity<ResultDTO> getAllUsers();

    ResponseEntity<ResultDTO> findUserById(Long id);

    ResponseEntity<ResultDTO> findUserByLogin(String login);

    ResponseEntity<ResultDTO> findUsersByLogin(String login);

    ResponseEntity<ResultDTO> editUserById(Long id, UserPayload userPayload);

    ResponseEntity<ResultDTO> editUserByLogin(String login, UserPayload userPayload);

    ResponseEntity<ResultDTO> deleteUserById(Long id);

    ResponseEntity<ResultDTO> deleteUserByLogin(String login);
}
