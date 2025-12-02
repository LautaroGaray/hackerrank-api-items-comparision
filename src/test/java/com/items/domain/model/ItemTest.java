package com.items.domain.model;

import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import static org.junit.jupiter.api.Assertions.*;

class ItemTest {

    @Test
    void shouldCreateItemWithAllFields() {
        Specification spec = new Specification("Dell", "XPS", "Silver", 2.5, "300x200", "Aluminum", 24);
        
        Item item = new Item(
            "item-123",
            "Laptop",
            "http://img.com/laptop.jpg",
            "Gaming laptop",
            BigDecimal.valueOf(1500.0),
            4.5,
            spec
        );

        assertEquals("item-123", item.id());
        assertEquals("Laptop", item.name());
        assertEquals("http://img.com/laptop.jpg", item.imageUrl());
        assertEquals("Gaming laptop", item.description());
        assertEquals(BigDecimal.valueOf(1500.0), item.price());
        assertEquals(4.5, item.rating());
        assertNotNull(item.specification());
        assertEquals("Dell", item.specification().brand());
    }

    @Test
    void shouldCreateItemWithoutId() {
        Specification spec = new Specification("HP", "Pavilion", "Black", 2.0, "320x220", "Plastic", 12);
        
        Item item = new Item(
            "Mouse",
            "http://img.com/mouse.jpg",
            "Gaming mouse",
            BigDecimal.valueOf(50.0),
            4.8,
            spec
        );

        assertNull(item.id());
        assertEquals("Mouse", item.name());
        assertNotNull(item.specification());
    }

    @Test
    void shouldCreateItemWithNullSpecification() {
        Item item = new Item(
            "item-123",
            "Keyboard",
            "http://img.com/keyboard.jpg",
            "Mechanical keyboard",
            BigDecimal.valueOf(100.0),
            4.7,
            null
        );

        assertNull(item.specification());
    }

    @Test
    void withId_shouldCreateNewItemWithId() {
        Specification spec = new Specification("Lenovo", "ThinkPad", "Black", 1.8, "340x240", "Carbon", 36);
        
        Item original = new Item(
            null,
            "Laptop",
            "http://img.com/laptop.jpg",
            "Business laptop",
            BigDecimal.valueOf(1200.0),
            4.6,
            spec
        );

        Item withId = original.withId("generated-id");

        assertNull(original.id());
        assertEquals("generated-id", withId.id());
        assertEquals(original.name(), withId.name());
        assertEquals(original.specification(), withId.specification());
    }

    @Test
    void shouldSupportEquality() {
        Specification spec1 = new Specification("Brand", "Model", "Color", 2.0, "dims", "mat", 12);
        Specification spec2 = new Specification("Brand", "Model", "Color", 2.0, "dims", "mat", 12);
        
        Item item1 = new Item("id1", "name", "url", "desc", BigDecimal.TEN, 4.0, spec1);
        Item item2 = new Item("id1", "name", "url", "desc", BigDecimal.TEN, 4.0, spec2);

        assertEquals(item1, item2);
        assertEquals(item1.hashCode(), item2.hashCode());
    }

    @Test
    void shouldHaveProperToString() {
        Specification spec = new Specification("Dell", "XPS", "Silver", 2.5, "300x200", "Aluminum", 24);
        Item item = new Item("id1", "Laptop", "url", "desc", BigDecimal.valueOf(1500), 4.5, spec);
        
        String toString = item.toString();
        
        assertTrue(toString.contains("Laptop"));
        assertTrue(toString.contains("1500"));
    }
}