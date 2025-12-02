package com.items.infraestructure.adapters.inbound.rest.dto;

import org.junit.jupiter.api.Test;

import com.items.domain.model.Specification;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class CreateItemRequestTest {

    @Test
    void shouldCreateRequestWithAllFields() {
         Specification specification = new Specification("spec1", "Spec Description", null, null, null, null, null);
        CreateItemRequest request = new CreateItemRequest(
            "Laptop",
            "http://example.com/laptop.jpg",
            "Gaming laptop",
            BigDecimal.valueOf(1500.0),
            4.5,
            specification
        );

        assertEquals("Laptop", request.name());
        assertEquals("http://example.com/laptop.jpg", request.imageUrl());
        assertEquals("Gaming laptop", request.description());
        assertEquals(BigDecimal.valueOf(1500.0), request.price());
        assertEquals(4.5, request.rating());
    }

    @Test
    void shouldNotHaveIdField() {
         Specification specification = new Specification("spec1", "Spec Description", null, null, null, null, null);
        CreateItemRequest request = new CreateItemRequest(
            "Mouse",
            "http://example.com/mouse.jpg",
            "Gaming mouse",
            BigDecimal.valueOf(50.0),
            4.8,
            specification
        );

        
        assertThrows(NoSuchMethodException.class, () -> {
            request.getClass().getMethod("id");
        });
    }

    @Test
    void shouldBeImmutable() {
         Specification specification = new Specification("spec1", "Spec Description", null, null, null, null, null);
        CreateItemRequest request = new CreateItemRequest(
            "Keyboard",
            "http://example.com/keyboard.jpg",
            "Mechanical keyboard",
            BigDecimal.valueOf(100.0),
            4.7,
            specification
        );

        assertEquals("Keyboard", request.name());        
       
        assertThrows(NoSuchMethodException.class, () -> {
            request.getClass().getMethod("setName", String.class);
        });
    }

    @Test
    void shouldSupportEquality() {
         Specification specification = new Specification("spec1", "Spec Description", null, null, null, null, null);
        CreateItemRequest request1 = new CreateItemRequest(
            "Mouse",
            "http://example.com/mouse.jpg",
            "Gaming mouse",
            BigDecimal.valueOf(50.0),
            4.8,
            specification
        );

        CreateItemRequest request2 = new CreateItemRequest(
            "Mouse",
            "http://example.com/mouse.jpg",
            "Gaming mouse",
            BigDecimal.valueOf(50.0),
            4.8,
            specification
        );

        assertEquals(request1, request2);
        assertEquals(request1.hashCode(), request2.hashCode());
    }

    @Test
    void shouldNotBeEqualWithDifferentValues() {
         Specification specification = new Specification("spec1", "Spec Description", null, null, null, null, null);
        CreateItemRequest request1 = new CreateItemRequest(
            "Mouse",
            "http://example.com/mouse.jpg",
            "Gaming mouse",
            BigDecimal.valueOf(50.0),
            4.8,
            specification
        );

        CreateItemRequest request2 = new CreateItemRequest(
            "Keyboard",
            "http://example.com/keyboard.jpg",
            "Mechanical keyboard",
            BigDecimal.valueOf(100.0),
            4.7,
            specification
        );

        assertNotEquals(request1, request2);
        assertNotEquals(request1.hashCode(), request2.hashCode());
    }

    @Test
    void shouldHaveProperToString() {
         Specification specification = new Specification("spec1", "Spec Description", null, null, null, null, null);
        CreateItemRequest request = new CreateItemRequest(
            "Monitor",
            "http://example.com/monitor.jpg",
            "4K Monitor",
            BigDecimal.valueOf(300.0),
            4.6,
            specification
        );

        String toString = request.toString();
        
        assertTrue(toString.contains("Monitor"));
        assertTrue(toString.contains("http://example.com/monitor.jpg"));
        assertTrue(toString.contains("4K Monitor"));
        assertTrue(toString.contains("300.0"));
        assertTrue(toString.contains("4.6"));
    }

    @Test
    void shouldHandleNullName() {
         Specification specification = new Specification("spec1", "Spec Description", null, null, null, null, null);
        CreateItemRequest request = new CreateItemRequest(
            null,
            "http://example.com/image.jpg",
            "Description",
            BigDecimal.valueOf(100.0),
            4.5,
            specification
        );

        assertNull(request.name());
    }

    @Test
    void shouldHandleNullImageUrl() {
         Specification specification = new Specification("spec1", "Spec Description", null, null, null, null, null);
        CreateItemRequest request = new CreateItemRequest(
            "Item",
            null,
            "Description",
            BigDecimal.valueOf(100.0),
            4.5,
            specification
        );

        assertNull(request.imageUrl());
    }

    @Test
    void shouldHandleNullDescription() {
         Specification specification = new Specification("spec1", "Spec Description", null, null, null, null, null);
        CreateItemRequest request = new CreateItemRequest(
            "Item",
            "http://example.com/image.jpg",
            null,
            BigDecimal.valueOf(100.0),
            4.5,
            specification
        );

        assertNull(request.description());
    }

    @Test
    void shouldHandleNullPrice() {
         Specification specification = new Specification("spec1", "Spec Description", null, null, null, null, null);
        CreateItemRequest request = new CreateItemRequest(
            "Item",
            "http://example.com/image.jpg",
            "Description",
            null,
            4.5,
            specification
        );

        assertNull(request.price());
    }

    @Test
    void shouldHandleNullRating() {
         Specification specification = new Specification("spec1", "Spec Description", null, null, null, null, null);
        CreateItemRequest request = new CreateItemRequest(
            "Item",
            "http://example.com/image.jpg",
            "Description",
            BigDecimal.valueOf(100.0),
            null,
            specification
        );

        assertNull(request.rating());
    }

    @Test
    void shouldCreateWithDifferentPriceFormats() {
         Specification specification = new Specification("spec1", "Spec Description", null, null, null, null, null);
        CreateItemRequest request1 = new CreateItemRequest(
            "Item",
            "http://example.com/image.jpg",
            "Description",
            new BigDecimal("99.99"),
            4.5,
            specification
        );

        CreateItemRequest request2 = new CreateItemRequest(
            "Item",
            "http://example.com/image.jpg",
            "Description",
            BigDecimal.valueOf(99.99),
            4.5,
            specification
        );

        assertEquals(request1.price().doubleValue(), request2.price().doubleValue(), 0.001);
    }

    @Test
    void shouldHandleZeroPrice() {
         Specification specification = new Specification("spec1", "Spec Description", null, null, null, null, null);
        CreateItemRequest request = new CreateItemRequest(
            "Free Item",
            "http://example.com/free.jpg",
            "Free description",
            BigDecimal.ZERO,
            4.0,
            specification
        );

        assertEquals(BigDecimal.ZERO, request.price());
    }

    @Test
    void shouldHandleMinimumRating() {
         Specification specification = new Specification("spec1", "Spec Description", null, null, null, null, null);
        CreateItemRequest request = new CreateItemRequest(
            "Bad Item",
            "http://example.com/bad.jpg",
            "Bad description",
            BigDecimal.valueOf(10.0),
            0.0,
            specification
        );

        assertEquals(0.0, request.rating());
    }

    @Test
    void shouldHandleMaximumRating() {
         Specification specification = new Specification("spec1", "Spec Description", null, null, null, null, null);
        CreateItemRequest request = new CreateItemRequest(
            "Perfect Item",
            "http://example.com/perfect.jpg",
            "Perfect description",
            BigDecimal.valueOf(100.0),
            5.0,
            specification
        );

        assertEquals(5.0, request.rating());
    }
}