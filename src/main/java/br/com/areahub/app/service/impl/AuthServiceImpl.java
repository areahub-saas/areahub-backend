package br.com.areahub.app.service.impl;

import br.com.areahub.app.dto.LoginRequestDTO;
import br.com.areahub.app.dto.RegisterRequestDTO;
import br.com.areahub.app.dto.UserResponseDTO;
import br.com.areahub.app.entities.User;
import br.com.areahub.app.mapper.UserMapper;
import br.com.areahub.app.repository.UserRepository;
import br.com.areahub.app.service.AuthService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final UserMapper userMapper;

    @Transactional
    @Override
    public UserResponseDTO registerUser(RegisterRequestDTO requestDTO) {
        var user = User.builder()
                .username(requestDTO.username())
                .email(requestDTO.email())
                .password(passwordEncoder.encode(requestDTO.password()))
                .build();
        return userMapper.userResponseDTO(userRepository.save(user));
    }

    @Override
    public String authenticationUser(LoginRequestDTO requestDTO) {
        return "";
    }
}
