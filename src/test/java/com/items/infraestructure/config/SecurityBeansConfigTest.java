package com.items.infraestructure.config;

import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;

class SecurityBeansConfigTest {

    private SecurityBeansConfig config = new SecurityBeansConfig();

    @Test
    void passwordEncoder_shouldReturnBCryptPasswordEncoder() {
        PasswordEncoder encoder = config.passwordEncoder();

        assertNotNull(encoder);
        assertTrue(encoder instanceof BCryptPasswordEncoder);
    }

    @Test
    void passwordEncoder_shouldEncodeDifferentPasswords() {
        PasswordEncoder encoder = config.passwordEncoder();

        String encoded1 = encoder.encode("password123");
        String encoded2 = encoder.encode("password456");

        assertNotEquals(encoded1, encoded2);
    }

    @Test
    void passwordEncoder_shouldMatchEncodedPassword() {
        PasswordEncoder encoder = config.passwordEncoder();
        String rawPassword = "myPassword";

        String encoded = encoder.encode(rawPassword);

        assertTrue(encoder.matches(rawPassword, encoded));
    }

    @Test
    void passwordEncoder_shouldNotMatchDifferentPassword() {
        PasswordEncoder encoder = config.passwordEncoder();
        String rawPassword = "myPassword";
        String wrongPassword = "wrongPassword";

        String encoded = encoder.encode(rawPassword);

        assertFalse(encoder.matches(wrongPassword, encoded));
    }

    @Test
    void passwordEncoder_shouldProduceDifferentHashesForSamePassword() {
        PasswordEncoder encoder = config.passwordEncoder();
        String password = "samePassword";

        String hash1 = encoder.encode(password);
        String hash2 = encoder.encode(password);

        assertNotEquals(hash1, hash2);
        assertTrue(encoder.matches(password, hash1));
        assertTrue(encoder.matches(password, hash2));
    }

    @Test
    void passwordEncoder_shouldHandleEmptyPassword() {
        PasswordEncoder encoder = config.passwordEncoder();

        String encoded = encoder.encode("");

        assertNotNull(encoded);
        assertTrue(encoder.matches("", encoded));
    }

    @Test
    void passwordEncoder_shouldHandleSpecialCharacters() {
        PasswordEncoder encoder = config.passwordEncoder();
        String password = "p@ssw0rd!#$%";

        String encoded = encoder.encode(password);

        assertTrue(encoder.matches(password, encoded));
    }

    @Test
    void constructor_shouldInitialize() {
        SecurityBeansConfig securityConfig = new SecurityBeansConfig();

        assertNotNull(securityConfig);
    }
}