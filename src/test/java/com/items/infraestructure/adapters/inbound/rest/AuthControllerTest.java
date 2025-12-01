package com.items.infraestructure.adapters.inbound.rest;

import com.items.application.service.AuthService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthControllerTest {

    @Mock
    private AuthService authService;

    @InjectMocks
    private AuthController authController;

    @BeforeEach
    void setUp() {
        authController = new AuthController(authService);
    }

    @Test
    void login_withValidCredentials_shouldReturnToken() {
        AuthController.AuthRequest request = new AuthController.AuthRequest("admin", "adminpass");
        when(authService.authenticate("admin", "adminpass")).thenReturn("jwt-token-xyz");

        Map<String, String> response = authController.login(request);

        assertNotNull(response);
        assertEquals("jwt-token-xyz", response.get("token"));
        verify(authService).authenticate("admin", "adminpass");
    }

    @Test
    void login_shouldCallAuthServiceOnce() {
        AuthController.AuthRequest request = new AuthController.AuthRequest("user", "pass");
        when(authService.authenticate("user", "pass")).thenReturn("token");

        authController.login(request);

        verify(authService, times(1)).authenticate("user", "pass");
    }

    @Test
    void login_withDifferentUsers_shouldReturnDifferentTokens() {
        AuthController.AuthRequest request1 = new AuthController.AuthRequest("user1", "pass1");
        AuthController.AuthRequest request2 = new AuthController.AuthRequest("user2", "pass2");
        
        when(authService.authenticate("user1", "pass1")).thenReturn("token1");
        when(authService.authenticate("user2", "pass2")).thenReturn("token2");

        Map<String, String> response1 = authController.login(request1);
        Map<String, String> response2 = authController.login(request2);

        assertEquals("token1", response1.get("token"));
        assertEquals("token2", response2.get("token"));
    }

    @Test
    void login_whenAuthServiceThrows_shouldPropagateException() {
        AuthController.AuthRequest request = new AuthController.AuthRequest("bad", "credentials");
        when(authService.authenticate("bad", "credentials")).thenThrow(new RuntimeException("Invalid credentials"));

        assertThrows(RuntimeException.class, () -> authController.login(request));
        verify(authService).authenticate("bad", "credentials");
    }

    @Test
    void authRequest_shouldCreateRecordCorrectly() {
        AuthController.AuthRequest request = new AuthController.AuthRequest("testuser", "testpass");

        assertEquals("testuser", request.username());
        assertEquals("testpass", request.password());
    }

    @Test
    void authRequest_shouldSupportEquality() {
        AuthController.AuthRequest request1 = new AuthController.AuthRequest("user", "pass");
        AuthController.AuthRequest request2 = new AuthController.AuthRequest("user", "pass");
        AuthController.AuthRequest request3 = new AuthController.AuthRequest("other", "pass");

        assertEquals(request1, request2);
        assertNotEquals(request1, request3);
    }
}