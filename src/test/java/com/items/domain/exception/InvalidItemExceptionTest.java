package com.items.domain.exception;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class InvalidItemExceptionTest {

    @Test
    void shouldCreateExceptionWithMessage() {
        String message = "Invalid item data";
        InvalidItemException exception = new InvalidItemException(message);
        
        assertEquals(message, exception.getMessage());
        assertTrue(exception instanceof RuntimeException);
    }

    @Test
    void shouldBeThrowable() {
        assertThrows(InvalidItemException.class, () -> {
            throw new InvalidItemException("Test exception");
        });
    }
}