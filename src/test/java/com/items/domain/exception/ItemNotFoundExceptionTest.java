package com.items.domain.exception;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ItemNotFoundExceptionTest {

    @Test
    void shouldCreateExceptionWithId() {
        String id = "123e4567-e89b-12d3-a456-426614174000";
        ItemNotFoundException exception = new ItemNotFoundException(id);
        
        String expectedMessage = "Item with id " + id + " not found";
        assertEquals(expectedMessage, exception.getMessage());
        assertTrue(exception instanceof RuntimeException);
    }

    @Test
    void shouldBeThrowable() {
        String id = "test-id";
        assertThrows(ItemNotFoundException.class, () -> {
            throw new ItemNotFoundException(id);
        });
    }
}