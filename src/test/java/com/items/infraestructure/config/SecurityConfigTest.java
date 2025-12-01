package com.items.infraestructure.config;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SecurityConfigTest {

    @Test
    void constructor_shouldCreateInstance() {
        SecurityConfig config = new SecurityConfig();
        
        assertNotNull(config);
    }

    @Test
    void securityConfig_shouldBeInstantiable() {
        assertDoesNotThrow(() -> new SecurityConfig());
    }
}