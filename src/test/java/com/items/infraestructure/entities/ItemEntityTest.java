package com.items.infraestructure.entities;

import com.items.domain.model.Item;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class ItemEntityTest {

    @Test
    void constructor_withNoArgs_shouldCreateEmptyEntity() {
        ItemEntity entity = new ItemEntity();

        assertNotNull(entity);
        assertNull(entity.getId());
        assertNull(entity.getName());
        assertNull(entity.getImageUrl());
        assertNull(entity.getDescription());
        assertNull(entity.getPrice());
        assertNull(entity.getRating());
    }

    @Test
    void constructor_withAllArgs_shouldCreateEntityWithValues() {
        String id = "item-123";
        String name = "Laptop";
        String imageUrl = "http://example.com/image.jpg";
        String description = "Gaming laptop";
        BigDecimal price = BigDecimal.valueOf(1500.00);
        Double rating = 4.5;

        ItemEntity entity = new ItemEntity(id, name, imageUrl, description, price, rating);

        assertEquals(id, entity.getId());
        assertEquals(name, entity.getName());
        assertEquals(imageUrl, entity.getImageUrl());
        assertEquals(description, entity.getDescription());
        assertEquals(price, entity.getPrice());
        assertEquals(rating, entity.getRating());
    }

    @Test
    void settersAndGetters_shouldWorkCorrectly() {
        ItemEntity entity = new ItemEntity();

        entity.setId("id-1");
        entity.setName("Mouse");
        entity.setImageUrl("http://img.url");
        entity.setDescription("Wireless mouse");
        entity.setPrice(BigDecimal.valueOf(25.99));
        entity.setRating(4.8);

        assertEquals("id-1", entity.getId());
        assertEquals("Mouse", entity.getName());
        assertEquals("http://img.url", entity.getImageUrl());
        assertEquals("Wireless mouse", entity.getDescription());
        assertEquals(BigDecimal.valueOf(25.99), entity.getPrice());
        assertEquals(4.8, entity.getRating());
    }

    @Test
    void fromDomain_withValidItem_shouldCreateEntity() {
        Item item = new Item("item-456", "Keyboard", "http://kb.jpg", "Mechanical keyboard", BigDecimal.valueOf(120.00), 4.7);

        ItemEntity entity = ItemEntity.fromDomain(item);

        assertNotNull(entity);
        assertEquals("item-456", entity.getId());
        assertEquals("Keyboard", entity.getName());
        assertEquals("http://kb.jpg", entity.getImageUrl());
        assertEquals("Mechanical keyboard", entity.getDescription());
        assertEquals(BigDecimal.valueOf(120.00), entity.getPrice());
        assertEquals(4.7, entity.getRating());
    }

    @Test
    void fromDomain_withNullItem_shouldReturnNull() {
        ItemEntity entity = ItemEntity.fromDomain(null);

        assertNull(entity);
    }

    @Test
    void fromDomain_withNullOptionalFields_shouldCreateEntity() {
        Item item = new Item("item-789", "Monitor", null, null, BigDecimal.valueOf(300.00), 4.3);

        ItemEntity entity = ItemEntity.fromDomain(item);

        assertNotNull(entity);
        assertEquals("item-789", entity.getId());
        assertEquals("Monitor", entity.getName());
        assertNull(entity.getImageUrl());
        assertNull(entity.getDescription());
        assertEquals(BigDecimal.valueOf(300.00), entity.getPrice());
        assertEquals(4.3, entity.getRating());
    }

    @Test
    void toDomain_shouldCreateItemFromEntity() {
        ItemEntity entity = new ItemEntity("item-999", "Headphones", "http://hp.jpg", "Noise cancelling", BigDecimal.valueOf(200.00), 4.9);

        Item item = entity.toDomain();

        assertNotNull(item);
        assertEquals("item-999", item.id());
        assertEquals("Headphones", item.name());
        assertEquals("http://hp.jpg", item.imageUrl());
        assertEquals("Noise cancelling", item.description());
        assertEquals(BigDecimal.valueOf(200.00), item.price());
        assertEquals(4.9, item.rating());
    }

    @Test
    void toDomain_withNullFields_shouldCreateItem() {
        ItemEntity entity = new ItemEntity();
        entity.setId("item-111");
        entity.setName("Phone");

        Item item = entity.toDomain();

        assertNotNull(item);
        assertEquals("item-111", item.id());
        assertEquals("Phone", item.name());
        assertNull(item.imageUrl());
        assertNull(item.description());
        assertNull(item.price());
        assertNull(item.rating());
    }

    @Test
    void fromDomainAndToDomain_shouldBeReversible() {
        Item originalItem = new Item("item-222", "Tablet", "http://tablet.jpg", "10 inch tablet", BigDecimal.valueOf(400.00), 4.6);

        ItemEntity entity = ItemEntity.fromDomain(originalItem);
        Item convertedItem = entity.toDomain();

        assertEquals(originalItem.id(), convertedItem.id());
        assertEquals(originalItem.name(), convertedItem.name());
        assertEquals(originalItem.imageUrl(), convertedItem.imageUrl());
        assertEquals(originalItem.description(), convertedItem.description());
        assertEquals(originalItem.price(), convertedItem.price());
        assertEquals(originalItem.rating(), convertedItem.rating());
    }

    @Test
    void setId_shouldUpdateId() {
        ItemEntity entity = new ItemEntity();
        entity.setId("new-id");

        assertEquals("new-id", entity.getId());
    }

    @Test
    void setName_shouldUpdateName() {
        ItemEntity entity = new ItemEntity();
        entity.setName("New Name");

        assertEquals("New Name", entity.getName());
    }

    @Test
    void setImageUrl_shouldUpdateImageUrl() {
        ItemEntity entity = new ItemEntity();
        entity.setImageUrl("http://new.url");

        assertEquals("http://new.url", entity.getImageUrl());
    }

    @Test
    void setDescription_shouldUpdateDescription() {
        ItemEntity entity = new ItemEntity();
        entity.setDescription("New description");

        assertEquals("New description", entity.getDescription());
    }

    @Test
    void setPrice_shouldUpdatePrice() {
        ItemEntity entity = new ItemEntity();
        BigDecimal newPrice = BigDecimal.valueOf(999.99);
        entity.setPrice(newPrice);

        assertEquals(newPrice, entity.getPrice());
    }

    @Test
    void setRating_shouldUpdateRating() {
        ItemEntity entity = new ItemEntity();
        entity.setRating(5.0);

        assertEquals(5.0, entity.getRating());
    }
}