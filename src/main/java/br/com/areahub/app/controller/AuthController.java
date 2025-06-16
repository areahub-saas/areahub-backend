package br.com.areahub.app.controller;

import br.com.areahub.app.dto.LoginRequestDTO;
import br.com.areahub.app.dto.RegisterRequestDTO;
import br.com.areahub.app.dto.UserResponseDTO;
import br.com.areahub.app.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/v1/auth")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<UserResponseDTO> registerUser(@Valid @RequestBody RegisterRequestDTO requestDTO){
        return ResponseEntity.ok(authService.registerUser(requestDTO));
    }

    @PostMapping("/login")
    public void authenticationUser(@Valid @RequestBody LoginRequestDTO auth){
    }
}
