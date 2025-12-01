package com.items.domain.model;

import org.junit.jupiter.api.Test;
import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class ItemTest {

    @Test
    void shouldCreateItemWithAllFields() {
        String id = "id1";
        String name = "Item A";
        String imageUrl = "http://example.com/a.jpg";
        String description = "Description A";
        BigDecimal price = BigDecimal.valueOf(100.0);
        Double rating = 4.5;

        Item item = new Item(id, name, imageUrl, description, price, rating);

        assertEquals(id, item.id());
        assertEquals(name, item.name());
        assertEquals(imageUrl, item.imageUrl());
        assertEquals(description, item.description());
        assertEquals(price, item.price());
        assertEquals(rating, item.rating());
    }

    @Test
    void shouldAllowNullOptionalFields() {
        Item item = new Item("id1", "name", null, null, BigDecimal.TEN, 4.0);

        assertNotNull(item);
        assertNull(item.imageUrl());
        assertNull(item.description());
    }

    @Test
    void shouldSupportRecordEquality() {
        Item item1 = new Item("id1", "name", "url", "desc", BigDecimal.TEN, 4.0);
        Item item2 = new Item("id1", "name", "url", "desc", BigDecimal.TEN, 4.0);
        Item item3 = new Item("id2", "name", "url", "desc", BigDecimal.TEN, 4.0);

        assertEquals(item1, item2);
        assertNotEquals(item1, item3);
    }

    @Test
    void shouldGenerateCorrectHashCode() {
        Item item1 = new Item("id1", "name", "url", "desc", BigDecimal.TEN, 4.0);
        Item item2 = new Item("id1", "name", "url", "desc", BigDecimal.TEN, 4.0);

        assertEquals(item1.hashCode(), item2.hashCode());
    }

    @Test
    void shouldGenerateCorrectToString() {
        Item item = new Item("id1", "name", "url", "desc", BigDecimal.TEN, 4.0);
        String toString = item.toString();

        assertTrue(toString.contains("id1"));
        assertTrue(toString.contains("name"));
        assertTrue(toString.contains("url"));
        assertTrue(toString.contains("desc"));
    }
}