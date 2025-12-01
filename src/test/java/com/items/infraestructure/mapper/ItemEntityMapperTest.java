package com.items.infraestructure.mapper;

import com.items.domain.model.Item;
import com.items.infraestructure.entities.ItemEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class ItemEntityMapperTest {

    private ItemEntityMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = new ItemEntityMapper();
    }

    @Test
    void toEntity_withValidItem_shouldMapToEntity() {
        Item item = new Item("item-123", "Laptop", "http://img.com/laptop.jpg", "Gaming laptop", BigDecimal.valueOf(1500.00), 4.5);

        ItemEntity entity = mapper.toEntity(item);

        assertNotNull(entity);
        assertEquals("item-123", entity.getId());
        assertEquals("Laptop", entity.getName());
        assertEquals("http://img.com/laptop.jpg", entity.getImageUrl());
        assertEquals("Gaming laptop", entity.getDescription());
        assertEquals(BigDecimal.valueOf(1500.00), entity.getPrice());
        assertEquals(4.5, entity.getRating());
    }

    @Test
    void toEntity_withNullOptionalFields_shouldMapToEntity() {
        Item item = new Item("item-456", "Mouse", null, null, BigDecimal.valueOf(25.99), 4.8);

        ItemEntity entity = mapper.toEntity(item);

        assertNotNull(entity);
        assertEquals("item-456", entity.getId());
        assertEquals("Mouse", entity.getName());
        assertNull(entity.getImageUrl());
        assertNull(entity.getDescription());
        assertEquals(BigDecimal.valueOf(25.99), entity.getPrice());
        assertEquals(4.8, entity.getRating());
    }

    @Test
    void toEntity_withAllFieldsNull_shouldMapToEntity() {
        Item item = new Item(null, null, null, null, null, null);

        ItemEntity entity = mapper.toEntity(item);

        assertNotNull(entity);
        assertNull(entity.getId());
        assertNull(entity.getName());
        assertNull(entity.getImageUrl());
        assertNull(entity.getDescription());
        assertNull(entity.getPrice());
        assertNull(entity.getRating());
    }

    @Test
    void toDomain_withValidEntity_shouldMapToItem() {
        ItemEntity entity = new ItemEntity("item-789", "Keyboard", "http://kb.jpg", "Mechanical keyboard", BigDecimal.valueOf(120.00), 4.7);

        Item item = mapper.toDomain(entity);

        assertNotNull(item);
        assertEquals("item-789", item.id());
        assertEquals("Keyboard", item.name());
        assertEquals("http://kb.jpg", item.imageUrl());
        assertEquals("Mechanical keyboard", item.description());
        assertEquals(BigDecimal.valueOf(120.00), item.price());
        assertEquals(4.7, item.rating());
    }

    @Test
    void toDomain_withNullOptionalFields_shouldMapToItem() {
        ItemEntity entity = new ItemEntity("item-999", "Monitor", null, null, BigDecimal.valueOf(300.00), 4.3);

        Item item = mapper.toDomain(entity);

        assertNotNull(item);
        assertEquals("item-999", item.id());
        assertEquals("Monitor", item.name());
        assertNull(item.imageUrl());
        assertNull(item.description());
        assertEquals(BigDecimal.valueOf(300.00), item.price());
        assertEquals(4.3, item.rating());
    }

    @Test
    void toDomain_withAllFieldsNull_shouldMapToItem() {
        ItemEntity entity = new ItemEntity();

        Item item = mapper.toDomain(entity);

        assertNotNull(item);
        assertNull(item.id());
        assertNull(item.name());
        assertNull(item.imageUrl());
        assertNull(item.description());
        assertNull(item.price());
        assertNull(item.rating());
    }

    @Test
    void toEntityAndToDomain_shouldBeReversible() {
        Item originalItem = new Item("item-111", "Headphones", "http://hp.jpg", "Noise cancelling", BigDecimal.valueOf(200.00), 4.9);

        ItemEntity entity = mapper.toEntity(originalItem);
        Item convertedItem = mapper.toDomain(entity);

        assertEquals(originalItem.id(), convertedItem.id());
        assertEquals(originalItem.name(), convertedItem.name());
        assertEquals(originalItem.imageUrl(), convertedItem.imageUrl());
        assertEquals(originalItem.description(), convertedItem.description());
        assertEquals(originalItem.price(), convertedItem.price());
        assertEquals(originalItem.rating(), convertedItem.rating());
    }

    @Test
    void toDomainAndToEntity_shouldBeReversible() {
        ItemEntity originalEntity = new ItemEntity("item-222", "Tablet", "http://tablet.jpg", "10 inch tablet", BigDecimal.valueOf(400.00), 4.6);

        Item item = mapper.toDomain(originalEntity);
        ItemEntity convertedEntity = mapper.toEntity(item);

        assertEquals(originalEntity.getId(), convertedEntity.getId());
        assertEquals(originalEntity.getName(), convertedEntity.getName());
        assertEquals(originalEntity.getImageUrl(), convertedEntity.getImageUrl());
        assertEquals(originalEntity.getDescription(), convertedEntity.getDescription());
        assertEquals(originalEntity.getPrice(), convertedEntity.getPrice());
        assertEquals(originalEntity.getRating(), convertedEntity.getRating());
    }

    @Test
    void toEntity_withZeroPrice_shouldMapCorrectly() {
        Item item = new Item("item-333", "Free Item", "url", "desc", BigDecimal.ZERO, 3.0);

        ItemEntity entity = mapper.toEntity(item);

        assertEquals(BigDecimal.ZERO, entity.getPrice());
    }

    @Test
    void toEntity_withZeroRating_shouldMapCorrectly() {
        Item item = new Item("item-444", "Unrated", "url", "desc", BigDecimal.TEN, 0.0);

        ItemEntity entity = mapper.toEntity(item);

        assertEquals(0.0, entity.getRating());
    }

    @Test
    void toDomain_withZeroPrice_shouldMapCorrectly() {
        ItemEntity entity = new ItemEntity("item-555", "Free", "url", "desc", BigDecimal.ZERO, 3.5);

        Item item = mapper.toDomain(entity);

        assertEquals(BigDecimal.ZERO, item.price());
    }

    @Test
    void toDomain_withZeroRating_shouldMapCorrectly() {
        ItemEntity entity = new ItemEntity("item-666", "No Rating", "url", "desc", BigDecimal.TEN, 0.0);

        Item item = mapper.toDomain(entity);

        assertEquals(0.0, item.rating());
    }

    @Test
    void toEntity_withEmptyStrings_shouldMapCorrectly() {
        Item item = new Item("", "", "", "", BigDecimal.ONE, 1.0);

        ItemEntity entity = mapper.toEntity(item);

        assertEquals("", entity.getId());
        assertEquals("", entity.getName());
        assertEquals("", entity.getImageUrl());
        assertEquals("", entity.getDescription());
    }

    @Test
    void toDomain_withEmptyStrings_shouldMapCorrectly() {
        ItemEntity entity = new ItemEntity("", "", "", "", BigDecimal.ONE, 1.0);

        Item item = mapper.toDomain(entity);

        assertEquals("", item.id());
        assertEquals("", item.name());
        assertEquals("", item.imageUrl());
        assertEquals("", item.description());
    }
}