package com.items.domain.port.outbound;

import java.util.Optional;
import com.items.domain.model.User;

public interface UserRepositoryPort {
    Optional<User> findByUsername(String username);
}