package com.items.domain.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    @Test
    void shouldCreateUserWithAllFields() {
        String username = "admin";
        String password = "hashedPassword";
        String role = "ROLE_ADMIN";

        User user = new User(username, password, role);

        assertEquals(username, user.username());
        assertEquals(password, user.password());
        assertEquals(role, user.role());
    }

    @Test
    void shouldSupportRecordEquality() {
        User user1 = new User("admin", "pass", "ROLE_ADMIN");
        User user2 = new User("admin", "pass", "ROLE_ADMIN");
        User user3 = new User("user", "pass", "ROLE_USER");

        assertEquals(user1, user2);
        assertNotEquals(user1, user3);
    }

    @Test
    void shouldGenerateCorrectHashCode() {
        User user1 = new User("admin", "pass", "ROLE_ADMIN");
        User user2 = new User("admin", "pass", "ROLE_ADMIN");

        assertEquals(user1.hashCode(), user2.hashCode());
    }
}