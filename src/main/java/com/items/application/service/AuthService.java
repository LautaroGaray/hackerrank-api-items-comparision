package com.items.application.service;

import org.springframework.stereotype.Service;
import com.items.domain.port.outbound.UserRepositoryPort;
import com.items.domain.port.outbound.TokenProviderPort;
import com.items.domain.model.User;
import org.springframework.security.crypto.password.PasswordEncoder;

@Service
public class AuthService {
    private final UserRepositoryPort userRepo;
    private final TokenProviderPort tokenProvider;
    private final PasswordEncoder passwordEncoder;

    public AuthService(UserRepositoryPort userRepo, TokenProviderPort tokenProvider, PasswordEncoder passwordEncoder) {
        this.userRepo = userRepo;
        this.tokenProvider = tokenProvider;
        this.passwordEncoder = passwordEncoder;
    }

    public String authenticate(String username, String password) {
        User user = userRepo.findByUsername(username).orElseThrow(() -> new RuntimeException("Invalid credentials"));
        if (!passwordEncoder.matches(password, user.password())) throw new RuntimeException("Invalid credentials");
        return tokenProvider.generateToken(username);
    }
}