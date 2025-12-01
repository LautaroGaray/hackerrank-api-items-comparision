package com.items.application.service;

import com.items.domain.model.User;
import com.items.domain.port.outbound.TokenProviderPort;
import com.items.domain.port.outbound.UserRepositoryPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UserRepositoryPort userRepository;

    @Mock
    private TokenProviderPort tokenProvider;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private AuthService authService;

    private User testUser;

    @BeforeEach
    void setUp() {
        testUser = new User("testuser", "$hashed$password$", "ROLE_USER");
    }

    @Test
    void constructor_shouldInitializeCorrectly() {
        AuthService service = new AuthService(userRepository, tokenProvider, passwordEncoder);
        assertNotNull(service);
    }

    @Test
    void authenticate_withValidCredentials_shouldReturnToken() {
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(testUser));
        when(passwordEncoder.matches("rawpass", testUser.password())).thenReturn(true);
        when(tokenProvider.generateToken("testuser")).thenReturn("jwt-token-xyz");

        String token = authService.authenticate("testuser", "rawpass");

        assertEquals("jwt-token-xyz", token);
        verify(userRepository).findByUsername("testuser");
        verify(passwordEncoder).matches("rawpass", testUser.password());
        verify(tokenProvider).generateToken("testuser");
    }

    @Test
    void authenticate_withNonExistingUser_shouldThrowRuntimeException() {
        when(userRepository.findByUsername("unknown")).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, 
            () -> authService.authenticate("unknown", "anypass"));
        
        assertEquals("Invalid credentials", exception.getMessage());
        verify(userRepository).findByUsername("unknown");
        verify(passwordEncoder, never()).matches(anyString(), anyString());
        verify(tokenProvider, never()).generateToken(anyString());
    }

    @Test
    void authenticate_withWrongPassword_shouldThrowRuntimeException() {
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(testUser));
        when(passwordEncoder.matches("wrongpass", testUser.password())).thenReturn(false);

        RuntimeException exception = assertThrows(RuntimeException.class, 
            () -> authService.authenticate("testuser", "wrongpass"));
        
        assertEquals("Invalid credentials", exception.getMessage());
        verify(passwordEncoder).matches("wrongpass", testUser.password());
        verify(tokenProvider, never()).generateToken(anyString());
    }

    @Test
    void authenticate_withEmptyUsername_shouldThrowRuntimeException() {
        when(userRepository.findByUsername("")).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> authService.authenticate("", "pass"));
        verify(userRepository).findByUsername("");
    }

    @Test
    void authenticate_withNullPassword_shouldThrowRuntimeException() {
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(testUser));
        when(passwordEncoder.matches(null, testUser.password())).thenReturn(false);

        assertThrows(RuntimeException.class, () -> authService.authenticate("testuser", null));
    }

    @Test
    void authenticate_withEmptyPassword_shouldThrowRuntimeException() {
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(testUser));
        when(passwordEncoder.matches("", testUser.password())).thenReturn(false);

        assertThrows(RuntimeException.class, () -> authService.authenticate("testuser", ""));
        verify(passwordEncoder).matches("", testUser.password());
    }

    @Test
    void authenticate_shouldCallRepositoryOnce() {
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(testUser));
        when(passwordEncoder.matches("pass", testUser.password())).thenReturn(true);
        when(tokenProvider.generateToken("testuser")).thenReturn("token");

        authService.authenticate("testuser", "pass");

        verify(userRepository, times(1)).findByUsername("testuser");
    }

    @Test
    void authenticate_shouldCallPasswordEncoderOnce() {
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(testUser));
        when(passwordEncoder.matches("pass", testUser.password())).thenReturn(true);
        when(tokenProvider.generateToken("testuser")).thenReturn("token");

        authService.authenticate("testuser", "pass");

        verify(passwordEncoder, times(1)).matches("pass", testUser.password());
    }

    @Test
    void authenticate_shouldCallTokenProviderOnce() {
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(testUser));
        when(passwordEncoder.matches("pass", testUser.password())).thenReturn(true);
        when(tokenProvider.generateToken("testuser")).thenReturn("token");

        authService.authenticate("testuser", "pass");

        verify(tokenProvider, times(1)).generateToken("testuser");
    }
}