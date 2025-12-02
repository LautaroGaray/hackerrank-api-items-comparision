package com.items.infraestructure.entities;

import com.items.domain.model.Item;
import com.items.domain.model.Specification;
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
        assertNull(entity.getSpecification());
    }

    @Test
    void constructor_withAllArgs_shouldCreateEntityWithValues() {
        String id = "item-123";
        String name = "Laptop";
        String imageUrl = "http://example.com/image.jpg";
        String description = "Gaming laptop";
        BigDecimal price = BigDecimal.valueOf(1500.00);
        Double rating = 4.5;
        Specification spec = new Specification("Dell", "XPS 15", "Silver", 2.5, "357x235x18", "Aluminum", 24);

        ItemEntity entity = new ItemEntity(id, name, imageUrl, description, price, rating, spec);

        assertEquals(id, entity.getId());
        assertEquals(name, entity.getName());
        assertEquals(imageUrl, entity.getImageUrl());
        assertEquals(description, entity.getDescription());
        assertEquals(price, entity.getPrice());
        assertEquals(rating, entity.getRating());
        assertNotNull(entity.getSpecification());
        assertEquals("Dell", entity.getSpecification().brand());
    }

    @Test
    void constructor_withNullSpecification_shouldCreateEntity() {
        ItemEntity entity = new ItemEntity("id-1", "Mouse", "http://img.url", "desc", BigDecimal.TEN, 4.5, null);

        assertNotNull(entity);
        assertNull(entity.getSpecification());
    }

    @Test
    void settersAndGetters_shouldWorkCorrectly() {
        ItemEntity entity = new ItemEntity();
        Specification spec = new Specification("HP", "Pavilion", "Black", 2.0, "320x220x15", "Plastic", 12);

        entity.setId("id-1");
        entity.setName("Mouse");
        entity.setImageUrl("http://img.url");
        entity.setDescription("Wireless mouse");
        entity.setPrice(BigDecimal.valueOf(25.99));
        entity.setRating(4.8);
        entity.setSpecification(spec);

        assertEquals("id-1", entity.getId());
        assertEquals("Mouse", entity.getName());
        assertEquals("http://img.url", entity.getImageUrl());
        assertEquals("Wireless mouse", entity.getDescription());
        assertEquals(BigDecimal.valueOf(25.99), entity.getPrice());
        assertEquals(4.8, entity.getRating());
        assertNotNull(entity.getSpecification());
        assertEquals("HP", entity.getSpecification().brand());
    }

    @Test
    void fromDomain_withValidItem_shouldCreateEntity() {
        Specification spec = new Specification("Lenovo", "ThinkPad", "Black", 1.8, "340x240x16", "Carbon", 36);
        Item item = new Item("item-456", "Keyboard", "http://kb.jpg", "Mechanical keyboard", BigDecimal.valueOf(120.00), 4.7, spec);

        ItemEntity entity = ItemEntity.fromDomain(item);

        assertNotNull(entity);
        assertEquals("item-456", entity.getId());
        assertEquals("Keyboard", entity.getName());
        assertEquals("http://kb.jpg", entity.getImageUrl());
        assertEquals("Mechanical keyboard", entity.getDescription());
        assertEquals(BigDecimal.valueOf(120.00), entity.getPrice());
        assertEquals(4.7, entity.getRating());
        assertNotNull(entity.getSpecification());
        assertEquals("Lenovo", entity.getSpecification().brand());
    }

    @Test
    void fromDomain_withNullItem_shouldReturnNull() {
        ItemEntity entity = ItemEntity.fromDomain(null);

        assertNull(entity);
    }

    @Test
    void fromDomain_withNullOptionalFields_shouldCreateEntity() {
        Item item = new Item("item-789", "Monitor", null, null, BigDecimal.valueOf(300.00), 4.3, null);

        ItemEntity entity = ItemEntity.fromDomain(item);

        assertNotNull(entity);
        assertEquals("item-789", entity.getId());
        assertEquals("Monitor", entity.getName());
        assertNull(entity.getImageUrl());
        assertNull(entity.getDescription());
        assertEquals(BigDecimal.valueOf(300.00), entity.getPrice());
        assertEquals(4.3, entity.getRating());
        assertNull(entity.getSpecification());
    }

    @Test
    void fromDomain_withNullSpecification_shouldCreateEntity() {
        Item item = new Item("item-999", "Speaker", "http://sp.jpg", "Bluetooth speaker", BigDecimal.valueOf(80.00), 4.4, null);

        ItemEntity entity = ItemEntity.fromDomain(item);

        assertNotNull(entity);
        assertNull(entity.getSpecification());
    }

    @Test
    void toDomain_shouldCreateItemFromEntity() {
        Specification spec = new Specification("Sony", "WH-1000XM5", "Black", 0.25, "80x70x30", "Plastic", 24);
        ItemEntity entity = new ItemEntity("item-999", "Headphones", "http://hp.jpg", "Noise cancelling", BigDecimal.valueOf(200.00), 4.9, spec);

        Item item = entity.toDomain();

        assertNotNull(item);
        assertEquals("item-999", item.id());
        assertEquals("Headphones", item.name());
        assertEquals("http://hp.jpg", item.imageUrl());
        assertEquals("Noise cancelling", item.description());
        assertEquals(BigDecimal.valueOf(200.00), item.price());
        assertEquals(4.9, item.rating());
        assertNotNull(item.specification());
        assertEquals("Sony", item.specification().brand());
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
        assertNull(item.specification());
    }

    @Test
    void toDomain_withNullSpecification_shouldCreateItem() {
        ItemEntity entity = new ItemEntity("item-222", "Tablet", "http://tablet.jpg", "desc", BigDecimal.valueOf(400.00), 4.6, null);

        Item item = entity.toDomain();

        assertNotNull(item);
        assertNull(item.specification());
    }

    @Test
    void fromDomainAndToDomain_shouldBeReversible() {
        Specification spec = new Specification("Apple", "MacBook Pro", "Space Gray", 1.4, "304x212x15", "Aluminum", 12);
        Item originalItem = new Item("item-222", "Tablet", "http://tablet.jpg", "10 inch tablet", BigDecimal.valueOf(400.00), 4.6, spec);

        ItemEntity entity = ItemEntity.fromDomain(originalItem);
        Item convertedItem = entity.toDomain();

        assertEquals(originalItem.id(), convertedItem.id());
        assertEquals(originalItem.name(), convertedItem.name());
        assertEquals(originalItem.imageUrl(), convertedItem.imageUrl());
        assertEquals(originalItem.description(), convertedItem.description());
        assertEquals(originalItem.price(), convertedItem.price());
        assertEquals(originalItem.rating(), convertedItem.rating());
        assertEquals(originalItem.specification(), convertedItem.specification());
    }

    @Test
    void fromDomainAndToDomain_withNullSpecification_shouldBeReversible() {
        Item originalItem = new Item("item-333", "Camera", "http://cam.jpg", "4K camera", BigDecimal.valueOf(800.00), 4.8, null);

        ItemEntity entity = ItemEntity.fromDomain(originalItem);
        Item convertedItem = entity.toDomain();

        assertEquals(originalItem, convertedItem);
        assertNull(convertedItem.specification());
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

    @Test
    void setSpecification_shouldUpdateSpecification() {
        ItemEntity entity = new ItemEntity();
        Specification spec = new Specification("Samsung", "Galaxy", "White", 0.18, "160x75x8", "Glass", 24);
        
        entity.setSpecification(spec);

        assertNotNull(entity.getSpecification());
        assertEquals("Samsung", entity.getSpecification().brand());
        assertEquals("Galaxy", entity.getSpecification().model());
    }

    @Test
    void setSpecification_withNull_shouldSetNull() {
        Specification spec = new Specification("Brand", "Model", "Color", 1.0, "dims", "mat", 12);
        ItemEntity entity = new ItemEntity("id", "name", "url", "desc", BigDecimal.TEN, 4.0, spec);
        
        entity.setSpecification(null);

        assertNull(entity.getSpecification());
    }

    @Test
    void getSpecification_shouldReturnSpecification() {
        Specification spec = new Specification("Asus", "ROG", "Red", 2.3, "350x250x20", "Metal", 24);
        ItemEntity entity = new ItemEntity();
        entity.setSpecification(spec);

        Specification result = entity.getSpecification();

        assertNotNull(result);
        assertEquals("Asus", result.brand());
        assertEquals("ROG", result.model());
    }
}