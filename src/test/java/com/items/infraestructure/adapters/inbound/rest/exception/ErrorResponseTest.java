package com.items.infraestructure.adapters.inbound.rest.exception;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ErrorResponseTest {

    @Test
    void shouldCreateErrorResponseWithCodeAndMessage() {
        String code = "INVALID_REQUEST";
        String message = "Invalid item data";

        ErrorResponse response = new ErrorResponse(code, message);

        assertEquals(code, response.code());
        assertEquals(message, response.message());
    }

    @Test
    void shouldCreateErrorResponseWithNullValues() {
        ErrorResponse response = new ErrorResponse(null, null);

        assertNull(response.code());
        assertNull(response.message());
    }

    @Test
    void shouldSupportRecordEquality() {
        ErrorResponse response1 = new ErrorResponse("NOT_FOUND", "Item not found");
        ErrorResponse response2 = new ErrorResponse("NOT_FOUND", "Item not found");
        ErrorResponse response3 = new ErrorResponse("INTERNAL_ERROR", "Error occurred");

        assertEquals(response1, response2);
        assertNotEquals(response1, response3);
    }

    @Test
    void shouldGenerateCorrectHashCode() {
        ErrorResponse response1 = new ErrorResponse("ERROR", "Message");
        ErrorResponse response2 = new ErrorResponse("ERROR", "Message");

        assertEquals(response1.hashCode(), response2.hashCode());
    }

    @Test
    void shouldGenerateCorrectToString() {
        ErrorResponse response = new ErrorResponse("TEST_CODE", "Test message");
        String toString = response.toString();

        assertTrue(toString.contains("TEST_CODE"));
        assertTrue(toString.contains("Test message"));
    }
}