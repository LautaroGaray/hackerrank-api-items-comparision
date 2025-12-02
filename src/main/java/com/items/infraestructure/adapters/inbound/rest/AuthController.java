package com.items.infraestructure.adapters.inbound.rest;

import org.springframework.web.bind.annotation.*;
import com.items.application.service.AuthService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/auth")
@Tag(name = "Authentication", description = "Authentication and authorization endpoints")
public class AuthController {
    private final AuthService authService;
    public AuthController(AuthService authService) { this.authService = authService; }

    @PostMapping("/login")
    @Operation(
        summary = "User login",
        description = "Authenticates a user and returns a JWT token to access protected endpoints"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200", 
            description = "Successful login - Returns JWT token"
        ),
        @ApiResponse(
            responseCode = "401", 
            description = "Invalid credentials"
        ),
        @ApiResponse(
            responseCode = "400", 
            description = "Invalid request"
        )
    })
    public java.util.Map<String,String> login(@RequestBody AuthRequest req) {
        String token = authService.authenticate(req.username(), req.password());
        return java.util.Map.of("token", token);
    }

    @Schema(description = "Authentication request")
    public static record AuthRequest(
        @Schema(description = "Username", example = "admin")
        String username, 
        @Schema(description = "Password", example = "adminpass")
        String password
    ) {}
}