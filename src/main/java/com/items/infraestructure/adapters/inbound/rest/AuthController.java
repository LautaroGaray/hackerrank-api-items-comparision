package com.items.infraestructure.adapters.inbound.rest;

import org.springframework.web.bind.annotation.*;
import com.items.application.service.AuthService;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private final AuthService authService;
    public AuthController(AuthService authService) { this.authService = authService; }

    @PostMapping("/login")
    public java.util.Map<String,String> login(@RequestBody AuthRequest req) {
        String token = authService.authenticate(req.username(), req.password());
        return java.util.Map.of("token", token);
    }

    public static record AuthRequest(String username, String password) {}
}