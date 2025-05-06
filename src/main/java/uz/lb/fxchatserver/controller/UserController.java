package uz.lb.fxchatserver.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.lb.fxchatserver.dto.ResultDTO;
import uz.lb.fxchatserver.payload.UserPayload;
import uz.lb.fxchatserver.service.implement.UserService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/server/user")
public class UserController {

    private final UserService userService;

    @GetMapping("/")
    public ResponseEntity<ResultDTO> getAll() {
        return userService.getAllUsers();
    }

    @PostMapping("/registration")
    public ResponseEntity<ResultDTO> createUser(@Valid @RequestBody UserPayload userPayload) {
        return userService.saveUser(userPayload);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResultDTO> findAllUserById(@Valid @PathVariable Long id) {
        return userService.findUserById(id);
    }

    @GetMapping("/login")
    public ResponseEntity<ResultDTO> findAllUserByLogin(@Valid @RequestParam String login) {
        return userService.findUserByLogin(login);
    }

    @GetMapping("/logins")
    public ResponseEntity<ResultDTO> findAllUsersByLogins(@Valid @RequestParam String login) {
        return userService.findUsersByLogin(login);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResultDTO> editUserById(@Valid @PathVariable Long id, @Valid @RequestBody UserPayload userPayload) {
        System.out.println("Assalom aleykum!..");
        return userService.editUserById(id, userPayload);
    }

    @PutMapping("/login")
    public ResponseEntity<ResultDTO> editUserByLogin(@Valid @RequestParam String login, @Valid @RequestBody UserPayload userPayload) {
        return userService.editUserByLogin(login, userPayload);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResultDTO> deleteUserById(@Valid @PathVariable Long id) {
        return userService.deleteUserById(id);
    }

    @DeleteMapping("/login")
    public ResponseEntity<ResultDTO> deleteUserByLogin(@Valid @RequestParam String login) {
        return userService.deleteUserByLogin(login);
    }
}

