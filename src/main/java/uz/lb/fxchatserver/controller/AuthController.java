package uz.lb.fxchatserver.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import uz.lb.fxchatserver.dto.ResultDTO;
import uz.lb.fxchatserver.dto.auth.AuthRequestDTO;
import uz.lb.fxchatserver.service.implement.UserService;

@Controller
@RequiredArgsConstructor
@RequestMapping("/fx-chat")
public class AuthController {
    private final UserService userService;
    @PostMapping("/auth")
    public ResponseEntity<ResultDTO> authorization(@RequestBody AuthRequestDTO dto) {
        return userService.authorization(dto);
    }
}
