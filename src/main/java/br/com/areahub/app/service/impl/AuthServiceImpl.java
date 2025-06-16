package br.com.areahub.app.service.impl;

import br.com.areahub.app.dto.LoginRequestDTO;
import br.com.areahub.app.dto.RegisterRequestDTO;
import br.com.areahub.app.dto.UserResponseDTO;
import br.com.areahub.app.entities.User;
import br.com.areahub.app.mapper.UserMapper;
import br.com.areahub.app.repository.UserRepository;
import br.com.areahub.app.service.AuthService;
import br.com.areahub.app.util.JwtUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@RequiredArgsConstructor
@Service
public class AuthServiceImpl implements AuthService, UserDetailsService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final UserMapper userMapper;

    private final JwtUtil jwtUtil;

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
        String username = requestDTO.username();
        String password = requestDTO.password();

        Authentication authentication = this.authentication(username, password);

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: "+ username));

        UUID organizationId = user.getOrganization() != null ? user.getOrganization().getPublicId() : null;

        return jwtUtil.createToken(authentication, organizationId);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("User not found with username: "+ username));
    }

    private Authentication authentication(String username, String password) {
        var userDetails = this.loadUserByUsername(username);

        if (userDetails == null){
            throw new BadCredentialsException("Invalid username or password");
        }

        if(!passwordEncoder.matches(password, userDetails.getPassword())){
            throw new BadCredentialsException("Invalid username or password");
        }

        return new UsernamePasswordAuthenticationToken(username, userDetails.getPassword(), userDetails.getAuthorities());
    }
}
