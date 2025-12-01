package com.items.infraestructure.adapters.outbound.persistence;

import com.items.domain.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class InMemoryUserRepositoryTest {

    @Mock
    private PasswordEncoder encoder;

    private InMemoryUserRepository userRepository;

    @BeforeEach
    void setup() {
        when(encoder.encode("adminpass")).thenReturn("$encoded$adminpass");
        userRepository = new InMemoryUserRepository(encoder);
    }

    @Test
    void constructor_shouldInitializeAdminUser() {
        verify(encoder).encode("adminpass");
    }

    @Test
    void findByUsername_whenAdminExists_shouldReturnAdmin() {
        Optional<User> result = userRepository.findByUsername("admin");

        assertTrue(result.isPresent());
        assertEquals("admin", result.get().username());
        assertEquals("$encoded$adminpass", result.get().password());
        assertEquals("ROLE_ADMIN", result.get().role());
    }

    @Test
    void findByUsername_whenUserDoesNotExist_shouldReturnEmpty() {
        Optional<User> result = userRepository.findByUsername("unknownuser");

        assertTrue(result.isEmpty());
    }

    @Test
    void findByUsername_withNullUsername_shouldReturnEmpty() {
        Optional<User> result = userRepository.findByUsername(null);

        assertTrue(result.isEmpty());
    }

    @Test
    void findByUsername_withEmptyUsername_shouldReturnEmpty() {
        Optional<User> result = userRepository.findByUsername("");

        assertTrue(result.isEmpty());
    }

    @Test
    void findByUsername_withDifferentCase_shouldReturnEmpty() {
        Optional<User> result = userRepository.findByUsername("Admin");
        Optional<User> result2 = userRepository.findByUsername("ADMIN");

        assertTrue(result.isEmpty());
        assertTrue(result2.isEmpty());
    }

    @Test
    void findByUsername_multipleCallsShouldReturnSameUser() {
        Optional<User> result1 = userRepository.findByUsername("admin");
        Optional<User> result2 = userRepository.findByUsername("admin");

        assertTrue(result1.isPresent());
        assertTrue(result2.isPresent());
        assertEquals(result1.get().username(), result2.get().username());
        assertEquals(result1.get().password(), result2.get().password());
    }

    @Test
    void repository_shouldStoreEncodedPassword() {
        Optional<User> admin = userRepository.findByUsername("admin");

        assertTrue(admin.isPresent());
        assertNotEquals("adminpass", admin.get().password());
        assertEquals("$encoded$adminpass", admin.get().password());
    }

    @Test
    void repository_shouldHaveCorrectRole() {
        Optional<User> admin = userRepository.findByUsername("admin");

        assertTrue(admin.isPresent());
        assertEquals("ROLE_ADMIN", admin.get().role());
    }
}