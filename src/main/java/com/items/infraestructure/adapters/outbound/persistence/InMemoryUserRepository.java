package com.items.infraestructure.adapters.outbound.persistence;

import org.springframework.stereotype.Repository;
import com.items.domain.port.outbound.UserRepositoryPort;
import com.items.domain.model.User;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Map;
import java.util.HashMap;
import java.util.Optional;

@Repository
public class InMemoryUserRepository implements UserRepositoryPort {
    private final Map<String, User> users = new HashMap<>();

    public InMemoryUserRepository(PasswordEncoder encoder) {
        users.put("admin", new User("admin", encoder.encode("adminpass"), "ROLE_ADMIN"));
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return Optional.ofNullable(users.get(username));
    }
}