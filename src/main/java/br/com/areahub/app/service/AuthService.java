package br.com.areahub.app.service;

import br.com.areahub.app.dto.LoginRequestDTO;
import br.com.areahub.app.dto.RegisterRequestDTO;
import br.com.areahub.app.dto.UserResponseDTO;

public interface AuthService {

    UserResponseDTO registerUser(RegisterRequestDTO requestDTO);

    String authenticationUser(LoginRequestDTO requestDTO);
}
