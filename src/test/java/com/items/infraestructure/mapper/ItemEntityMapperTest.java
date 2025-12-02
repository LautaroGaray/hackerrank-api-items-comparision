package com.items.infraestructure.mapper;

import com.items.domain.model.Item;
import com.items.domain.model.Specification;
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
        Specification spec = new Specification("Dell", "XPS 15", "Silver", 2.5, "357x235x18", "Aluminum", 24);
        Item item = new Item("item-123", "Laptop", "http://img.com/laptop.jpg", "Gaming laptop", BigDecimal.valueOf(1500.00), 4.5, spec);

        ItemEntity entity = mapper.toEntity(item);

        assertNotNull(entity);
        assertEquals("item-123", entity.getId());
        assertEquals("Laptop", entity.getName());
        assertEquals("http://img.com/laptop.jpg", entity.getImageUrl());
        assertEquals("Gaming laptop", entity.getDescription());
        assertEquals(BigDecimal.valueOf(1500.00), entity.getPrice());
        assertEquals(4.5, entity.getRating());
        assertNotNull(entity.getSpecification());
        assertEquals("Dell", entity.getSpecification().brand());
        assertEquals("XPS 15", entity.getSpecification().model());
    }

    @Test
    void toEntity_withNullSpecification_shouldMapToEntity() {
        Item item = new Item("item-456", "Mouse", "http://img.com/mouse.jpg", "Gaming mouse", BigDecimal.valueOf(25.99), 4.8, null);

        ItemEntity entity = mapper.toEntity(item);

        assertNotNull(entity);
        assertEquals("item-456", entity.getId());
        assertEquals("Mouse", entity.getName());
        assertNull(entity.getSpecification());
    }

    @Test
    void toEntity_withNullOptionalFields_shouldMapToEntity() {
        Item item = new Item("item-456", "Mouse", null, null, BigDecimal.valueOf(25.99), 4.8, null);

        ItemEntity entity = mapper.toEntity(item);

        assertNotNull(entity);
        assertEquals("item-456", entity.getId());
        assertEquals("Mouse", entity.getName());
        assertNull(entity.getImageUrl());
        assertNull(entity.getDescription());
        assertEquals(BigDecimal.valueOf(25.99), entity.getPrice());
        assertEquals(4.8, entity.getRating());
        assertNull(entity.getSpecification());
    }

    @Test
    void toEntity_withAllFieldsNull_shouldMapToEntity() {
        Item item = new Item(null, null, null, null, null, null, null);

        ItemEntity entity = mapper.toEntity(item);

        assertNotNull(entity);
        assertNull(entity.getId());
        assertNull(entity.getName());
        assertNull(entity.getImageUrl());
        assertNull(entity.getDescription());
        assertNull(entity.getPrice());
        assertNull(entity.getRating());
        assertNull(entity.getSpecification());
    }

    @Test
    void toDomain_withValidEntity_shouldMapToItem() {
        Specification spec = new Specification("HP", "Pavilion", "Black", 2.0, "320x220x15", "Plastic", 12);
        ItemEntity entity = new ItemEntity("item-789", "Keyboard", "http://kb.jpg", "Mechanical keyboard", BigDecimal.valueOf(120.00), 4.7, spec);

        Item item = mapper.toDomain(entity);

        assertNotNull(item);
        assertEquals("item-789", item.id());
        assertEquals("Keyboard", item.name());
        assertEquals("http://kb.jpg", item.imageUrl());
        assertEquals("Mechanical keyboard", item.description());
        assertEquals(BigDecimal.valueOf(120.00), item.price());
        assertEquals(4.7, item.rating());
        assertNotNull(item.specification());
        assertEquals("HP", item.specification().brand());
        assertEquals("Pavilion", item.specification().model());
    }

    @Test
    void toDomain_withNullSpecification_shouldMapToItem() {
        ItemEntity entity = new ItemEntity("item-999", "Monitor", "http://monitor.jpg", "4K monitor", BigDecimal.valueOf(300.00), 4.3, null);

        Item item = mapper.toDomain(entity);

        assertNotNull(item);
        assertEquals("item-999", item.id());
        assertEquals("Monitor", item.name());
        assertNull(item.specification());
    }

    @Test
    void toDomain_withNullOptionalFields_shouldMapToItem() {
        ItemEntity entity = new ItemEntity("item-999", "Monitor", null, null, BigDecimal.valueOf(300.00), 4.3, null);

        Item item = mapper.toDomain(entity);

        assertNotNull(item);
        assertEquals("item-999", item.id());
        assertEquals("Monitor", item.name());
        assertNull(item.imageUrl());
        assertNull(item.description());
        assertEquals(BigDecimal.valueOf(300.00), item.price());
        assertEquals(4.3, item.rating());
        assertNull(item.specification());
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
        assertNull(item.specification());
    }

    @Test
    void toEntityAndToDomain_shouldBeReversible() {
        Specification spec = new Specification("Sony", "WH-1000XM5", "Black", 0.25, "80x70x30", "Plastic", 24);
        Item originalItem = new Item("item-111", "Headphones", "http://hp.jpg", "Noise cancelling", BigDecimal.valueOf(200.00), 4.9, spec);

        ItemEntity entity = mapper.toEntity(originalItem);
        Item convertedItem = mapper.toDomain(entity);

        assertEquals(originalItem.id(), convertedItem.id());
        assertEquals(originalItem.name(), convertedItem.name());
        assertEquals(originalItem.imageUrl(), convertedItem.imageUrl());
        assertEquals(originalItem.description(), convertedItem.description());
        assertEquals(originalItem.price(), convertedItem.price());
        assertEquals(originalItem.rating(), convertedItem.rating());
        assertEquals(originalItem.specification(), convertedItem.specification());
    }

    @Test
    void toEntityAndToDomain_withNullSpecification_shouldBeReversible() {
        Item originalItem = new Item("item-222", "Speaker", "http://sp.jpg", "Bluetooth speaker", BigDecimal.valueOf(80.00), 4.4, null);

        ItemEntity entity = mapper.toEntity(originalItem);
        Item convertedItem = mapper.toDomain(entity);

        assertEquals(originalItem.id(), convertedItem.id());
        assertEquals(originalItem.name(), convertedItem.name());
        assertNull(convertedItem.specification());
    }

    @Test
    void toDomainAndToEntity_shouldBeReversible() {
        Specification spec = new Specification("Lenovo", "ThinkPad", "Black", 1.8, "340x240x16", "Carbon", 36);
        ItemEntity originalEntity = new ItemEntity("item-222", "Tablet", "http://tablet.jpg", "10 inch tablet", BigDecimal.valueOf(400.00), 4.6, spec);

        Item item = mapper.toDomain(originalEntity);
        ItemEntity convertedEntity = mapper.toEntity(item);

        assertEquals(originalEntity.getId(), convertedEntity.getId());
        assertEquals(originalEntity.getName(), convertedEntity.getName());
        assertEquals(originalEntity.getImageUrl(), convertedEntity.getImageUrl());
        assertEquals(originalEntity.getDescription(), convertedEntity.getDescription());
        assertEquals(originalEntity.getPrice(), convertedEntity.getPrice());
        assertEquals(originalEntity.getRating(), convertedEntity.getRating());
        assertEquals(originalEntity.getSpecification(), convertedEntity.getSpecification());
    }

    @Test
    void toDomainAndToEntity_withNullSpecification_shouldBeReversible() {
        ItemEntity originalEntity = new ItemEntity("item-333", "Camera", "http://cam.jpg", "4K camera", BigDecimal.valueOf(800.00), 4.8, null);

        Item item = mapper.toDomain(originalEntity);
        ItemEntity convertedEntity = mapper.toEntity(item);

        assertEquals(originalEntity.getId(), convertedEntity.getId());
        assertNull(convertedEntity.getSpecification());
    }

    @Test
    void toEntity_withZeroPrice_shouldMapCorrectly() {
        Item item = new Item("item-333", "Free Item", "url", "desc", BigDecimal.ZERO, 3.0, null);

        ItemEntity entity = mapper.toEntity(item);

        assertEquals(BigDecimal.ZERO, entity.getPrice());
    }

    @Test
    void toEntity_withZeroRating_shouldMapCorrectly() {
        Item item = new Item("item-444", "Unrated", "url", "desc", BigDecimal.TEN, 0.0, null);

        ItemEntity entity = mapper.toEntity(item);

        assertEquals(0.0, entity.getRating());
    }

    @Test
    void toDomain_withZeroPrice_shouldMapCorrectly() {
        ItemEntity entity = new ItemEntity("item-555", "Free", "url", "desc", BigDecimal.ZERO, 3.5, null);

        Item item = mapper.toDomain(entity);

        assertEquals(BigDecimal.ZERO, item.price());
    }

    @Test
    void toDomain_withZeroRating_shouldMapCorrectly() {
        ItemEntity entity = new ItemEntity("item-666", "No Rating", "url", "desc", BigDecimal.TEN, 0.0, null);

        Item item = mapper.toDomain(entity);

        assertEquals(0.0, item.rating());
    }

    @Test
    void toEntity_withEmptyStrings_shouldMapCorrectly() {
        Item item = new Item("", "", "", "", BigDecimal.ONE, 1.0, null);

        ItemEntity entity = mapper.toEntity(item);

        assertEquals("", entity.getId());
        assertEquals("", entity.getName());
        assertEquals("", entity.getImageUrl());
        assertEquals("", entity.getDescription());
        assertNull(entity.getSpecification());
    }

    @Test
    void toDomain_withEmptyStrings_shouldMapCorrectly() {
        ItemEntity entity = new ItemEntity("", "", "", "", BigDecimal.ONE, 1.0, null);

        Item item = mapper.toDomain(entity);

        assertEquals("", item.id());
        assertEquals("", item.name());
        assertEquals("", item.imageUrl());
        assertEquals("", item.description());
        assertNull(item.specification());
    }

    @Test
    void toEntity_withCompleteSpecification_shouldMapAllSpecFields() {
        Specification spec = new Specification("Asus", "ROG", "Red", 2.3, "350x250x20", "Metal", 24);
        Item item = new Item("item-777", "Gaming Laptop", "http://rog.jpg", "High-end gaming", BigDecimal.valueOf(2500.00), 4.9, spec);

        ItemEntity entity = mapper.toEntity(item);

        assertNotNull(entity.getSpecification());
        assertEquals("Asus", entity.getSpecification().brand());
        assertEquals("ROG", entity.getSpecification().model());
        assertEquals("Red", entity.getSpecification().color());
        assertEquals(2.3, entity.getSpecification().weight());
        assertEquals("350x250x20", entity.getSpecification().dimensions());
        assertEquals("Metal", entity.getSpecification().material());
        assertEquals(24, entity.getSpecification().warrantyMonths());
    }

    @Test
    void toDomain_withCompleteSpecification_shouldMapAllSpecFields() {
        Specification spec = new Specification("Apple", "MacBook Pro", "Space Gray", 1.4, "304x212x15", "Aluminum", 12);
        ItemEntity entity = new ItemEntity("item-888", "MacBook", "http://mac.jpg", "Professional laptop", BigDecimal.valueOf(3000.00), 5.0, spec);

        Item item = mapper.toDomain(entity);

        assertNotNull(item.specification());
        assertEquals("Apple", item.specification().brand());
        assertEquals("MacBook Pro", item.specification().model());
        assertEquals("Space Gray", item.specification().color());
        assertEquals(1.4, item.specification().weight());
        assertEquals("304x212x15", item.specification().dimensions());
        assertEquals("Aluminum", item.specification().material());
        assertEquals(12, item.specification().warrantyMonths());
    }

    @Test
    void toEntity_withPartialSpecification_shouldMapCorrectly() {
        Specification spec = new Specification("Samsung", null, null, null, null, null, null);
        Item item = new Item("item-999", "Phone", "http://phone.jpg", "Smartphone", BigDecimal.valueOf(700.00), 4.7, spec);

        ItemEntity entity = mapper.toEntity(item);

        assertNotNull(entity.getSpecification());
        assertEquals("Samsung", entity.getSpecification().brand());
        assertNull(entity.getSpecification().model());
        assertNull(entity.getSpecification().color());
    }

    @Test
    void toDomain_withPartialSpecification_shouldMapCorrectly() {
        Specification spec = new Specification("LG", "UltraGear", null, null, null, null, 24);
        ItemEntity entity = new ItemEntity("item-1000", "Monitor", "http://lg.jpg", "Gaming monitor", BigDecimal.valueOf(500.00), 4.6, spec);

        Item item = mapper.toDomain(entity);

        assertNotNull(item.specification());
        assertEquals("LG", item.specification().brand());
        assertEquals("UltraGear", item.specification().model());
        assertNull(item.specification().color());
        assertEquals(24, item.specification().warrantyMonths());
    }
}