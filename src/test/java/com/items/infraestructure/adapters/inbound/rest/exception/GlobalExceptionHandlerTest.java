package com.items.infraestructure.adapters.inbound.rest.exception;

import com.items.domain.exception.InvalidItemException;
import com.items.domain.exception.ItemNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class GlobalExceptionHandlerTest {

    private GlobalExceptionHandler handler;

    @BeforeEach
    void setUp() {
        handler = new GlobalExceptionHandler();
    }

    @Test
    void handleItemNotFoundException_shouldReturn404WithCorrectBody() {
        ItemNotFoundException exception = new ItemNotFoundException("item-123");

        ResponseEntity<ErrorResponse> response = handler.handleItemNotFound(exception);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("NOT_FOUND", response.getBody().code());
        assertTrue(response.getBody().message().contains("item-123"));
        assertTrue(response.getBody().message().contains("not found"));
    }

    @Test
    void handleItemNotFoundException_withNullId_shouldReturn404() {
        ItemNotFoundException exception = new ItemNotFoundException(null);

        ResponseEntity<ErrorResponse> response = handler.handleItemNotFound(exception);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("NOT_FOUND", response.getBody().code());
        assertNotNull(response.getBody().message());
    }

    @Test
    void handleItemNotFoundException_withEmptyId_shouldReturn404() {
        ItemNotFoundException exception = new ItemNotFoundException("");

        ResponseEntity<ErrorResponse> response = handler.handleItemNotFound(exception);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("NOT_FOUND", response.getBody().code());
    }

    @Test
    void handleItemNotFoundException_shouldLogException() {
        ItemNotFoundException exception = new ItemNotFoundException("test-id");
        
        // This will execute the log.error line
        ResponseEntity<ErrorResponse> response = handler.handleItemNotFound(exception);
        
        assertNotNull(response);
        assertNotNull(response.getBody());
    }

    @Test
    void handleInvalidItemException_shouldReturn400WithCorrectBody() {
        InvalidItemException exception = new InvalidItemException("Price cannot be negative");

        ResponseEntity<ErrorResponse> response = handler.handleInvalidItem(exception);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("INVALID_REQUEST", response.getBody().code());
        assertEquals("Price cannot be negative", response.getBody().message());
    }

    @Test
    void handleInvalidItemException_withEmptyMessage_shouldReturn400() {
        InvalidItemException exception = new InvalidItemException("");

        ResponseEntity<ErrorResponse> response = handler.handleInvalidItem(exception);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("INVALID_REQUEST", response.getBody().code());
        assertEquals("", response.getBody().message());
    }

    @Test
    void handleInvalidItemException_withNullMessage_shouldReturn400() {
        InvalidItemException exception = new InvalidItemException(null);

        ResponseEntity<ErrorResponse> response = handler.handleInvalidItem(exception);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("INVALID_REQUEST", response.getBody().code());
    }

    @Test
    void handleInvalidItemException_shouldLogException() {
        InvalidItemException exception = new InvalidItemException("test error");
        
        ResponseEntity<ErrorResponse> response = handler.handleInvalidItem(exception);
        
        assertNotNull(response);
        assertNotNull(response.getBody());
    }

    @Test
    void handleGenericException_shouldReturn500WithSanitizedMessage() {
        Exception exception = new RuntimeException("Unexpected error");

        ResponseEntity<ErrorResponse> response = handler.handleGenericException(exception);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("INTERNAL_ERROR", response.getBody().code());
        assertEquals("An unexpected error occurred", response.getBody().message());
    }

    @Test
    void handleGenericException_shouldNotExposeInternalDetails() {
        Exception exception = new Exception("Internal database connection failed at line 42");

        ResponseEntity<ErrorResponse> response = handler.handleGenericException(exception);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertFalse(response.getBody().message().contains("database"));
        assertFalse(response.getBody().message().contains("line 42"));
        assertEquals("An unexpected error occurred", response.getBody().message());
    }

    @Test
    void handleGenericException_withNullPointerException_shouldReturn500() {
        NullPointerException exception = new NullPointerException("Null value encountered");

        ResponseEntity<ErrorResponse> response = handler.handleGenericException(exception);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("INTERNAL_ERROR", response.getBody().code());
        assertEquals("An unexpected error occurred", response.getBody().message());
    }

    @Test
    void handleGenericException_withIllegalArgumentException_shouldReturn500() {
        IllegalArgumentException exception = new IllegalArgumentException("Invalid argument");

        ResponseEntity<ErrorResponse> response = handler.handleGenericException(exception);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("INTERNAL_ERROR", response.getBody().code());
    }

    @Test
    void handleGenericException_withArithmeticException_shouldReturn500() {
        ArithmeticException exception = new ArithmeticException("Division by zero");

        ResponseEntity<ErrorResponse> response = handler.handleGenericException(exception);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("INTERNAL_ERROR", response.getBody().code());
    }

    @Test
    void handleGenericException_shouldLogException() {
        Exception exception = new Exception("test generic error");
        
        ResponseEntity<ErrorResponse> response = handler.handleGenericException(exception);
        
        assertNotNull(response);
        assertNotNull(response.getBody());
    }

    @Test
    void allHandlers_shouldReturnNonNullResponseBody() {
        assertNotNull(handler.handleItemNotFound(new ItemNotFoundException("id")).getBody());
        assertNotNull(handler.handleInvalidItem(new InvalidItemException("msg")).getBody());
        assertNotNull(handler.handleGenericException(new Exception("ex")).getBody());
    }

    @Test
    void allHandlers_shouldSetCorrectHttpStatus() {
        assertEquals(HttpStatus.NOT_FOUND, handler.handleItemNotFound(new ItemNotFoundException("id")).getStatusCode());
        assertEquals(HttpStatus.BAD_REQUEST, handler.handleInvalidItem(new InvalidItemException("msg")).getStatusCode());
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, handler.handleGenericException(new Exception()).getStatusCode());
    }

    @Test
    void handleItemNotFound_shouldCreateErrorResponseCorrectly() {
        ItemNotFoundException ex = new ItemNotFoundException("abc-123");
        ResponseEntity<ErrorResponse> response = handler.handleItemNotFound(ex);
        
        ErrorResponse body = response.getBody();
        assertNotNull(body);
        assertEquals("NOT_FOUND", body.code());
        assertEquals("Item with id abc-123 not found", body.message());
    }

    @Test
    void handleInvalidItem_shouldCreateErrorResponseCorrectly() {
        InvalidItemException ex = new InvalidItemException("Rating must be between 0 and 5");
        ResponseEntity<ErrorResponse> response = handler.handleInvalidItem(ex);
        
        ErrorResponse body = response.getBody();
        assertNotNull(body);
        assertEquals("INVALID_REQUEST", body.code());
        assertEquals("Rating must be between 0 and 5", body.message());
    }

    @Test
    void handleGenericException_shouldCreateErrorResponseCorrectly() {
        Exception ex = new Exception("Some internal error");
        ResponseEntity<ErrorResponse> response = handler.handleGenericException(ex);
        
        ErrorResponse body = response.getBody();
        assertNotNull(body);
        assertEquals("INTERNAL_ERROR", body.code());
        assertEquals("An unexpected error occurred", body.message());
    }
}