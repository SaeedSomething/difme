package com.example.difme.service.auth;

import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.difme.model.UserModel;
import com.example.difme.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;

    @Override
    public String login(String userName, String password) {

        // TODO High Priority: Account locking and rate limiting
        // TODO Medium Priority: Input validation and HTTPS
        // TODO Low Priority: Refresh tokens and audit logging
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(userName, password));

        // If authentication successful, generate token
        UserModel user = userRepository.findByUserName(userName)
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setLastLogin(LocalDateTime.now());
        user.setFailedLoginAttempts(0);
        userRepository.save(user);
        return jwtService.generateToken(userName, user.getId());
    }

    @Override
    public UserModel authenticate(String token) {
        // This method is now handled by JwtAuthenticationFilter
        // But we can keep it for backward compatibility
        if (jwtService.isTokenValid(token)) {
            String username = jwtService.extractUsername(token);
            return userRepository.findByUserName(username).orElse(null);
        }
        return null;
    }
}