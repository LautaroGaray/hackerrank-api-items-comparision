package com.items.infraestructure.adapters.outbound.persistence;

import com.items.domain.model.Item;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class JsonItemRepositoryTest {

    @TempDir
    Path tempFolder;

    private JsonItemRepository repository;
    private Item sampleItem;

    @BeforeEach
    void setup() {
        repository = new JsonItemRepository(tempFolder.toString());
        sampleItem = new Item("sample-123", "Sample Product", "http://sample.img", "A sample description", BigDecimal.valueOf(49.99), 3.8);
    }

    @AfterEach
    void cleanup() {
        File[] allFiles = tempFolder.toFile().listFiles();
        if (allFiles != null) {
            for (File f : allFiles) {
                f.delete();
            }
        }
    }

    @Test
    void save_shouldCreateJsonFileWithCorrectName() {
        Item saved = repository.save(sampleItem);

        assertNotNull(saved);
        assertEquals(sampleItem.id(), saved.id());
        File jsonFile = new File(tempFolder.toFile(), sampleItem.id() + ".json");
        assertTrue(jsonFile.exists());
    }

    @Test
    void save_shouldPersistAllItemFields() throws IOException {
        repository.save(sampleItem);

        File jsonFile = new File(tempFolder.toFile(), sampleItem.id() + ".json");
        String content = Files.readString(jsonFile.toPath());
        
        assertTrue(content.contains("sample-123"));
        assertTrue(content.contains("Sample Product"));
        assertTrue(content.contains("http://sample.img"));
        assertTrue(content.contains("A sample description"));
        assertTrue(content.contains("49.99"));
        assertTrue(content.contains("3.8"));
    }

    @Test
    void save_shouldReturnSameItem() {
        Item saved = repository.save(sampleItem);

        assertEquals(sampleItem.id(), saved.id());
        assertEquals(sampleItem.name(), saved.name());
        assertEquals(sampleItem.imageUrl(), saved.imageUrl());
        assertEquals(sampleItem.description(), saved.description());
        assertEquals(sampleItem.price(), saved.price());
        assertEquals(sampleItem.rating(), saved.rating());
    }

    @Test
    void save_shouldOverwriteExistingFile() {
        repository.save(sampleItem);
        
        Item updated = new Item(sampleItem.id(), "Updated Name", "http://new.url", "New desc", BigDecimal.valueOf(99.99), 4.5);
        repository.save(updated);

        Optional<Item> result = repository.findById(sampleItem.id());
        assertTrue(result.isPresent());
        assertEquals("Updated Name", result.get().name());
        assertEquals(BigDecimal.valueOf(99.99), result.get().price());
    }

    @Test
    void findById_whenItemExists_shouldReturnItem() {
        repository.save(sampleItem);

        Optional<Item> result = repository.findById(sampleItem.id());

        assertTrue(result.isPresent());
        assertEquals(sampleItem.id(), result.get().id());
        assertEquals(sampleItem.name(), result.get().name());
        assertEquals(sampleItem.price(), result.get().price());
        assertEquals(sampleItem.rating(), result.get().rating());
    }

    @Test
    void findById_whenItemDoesNotExist_shouldReturnEmpty() {
        Optional<Item> result = repository.findById("nonexistent-id");

        assertTrue(result.isEmpty());
    }

    @Test
    void findById_withNullId_shouldReturnEmpty() {
        Optional<Item> result = repository.findById(null);

        assertTrue(result.isEmpty());
    }

    @Test
    void findById_withEmptyId_shouldReturnEmpty() {
        Optional<Item> result = repository.findById("");

        assertTrue(result.isEmpty());
    }

    @Test
    void findById_shouldDeserializeCorrectly() {
        repository.save(sampleItem);

        Optional<Item> result = repository.findById(sampleItem.id());

        assertTrue(result.isPresent());
        Item found = result.get();
        assertEquals(sampleItem.id(), found.id());
        assertEquals(sampleItem.name(), found.name());
        assertEquals(sampleItem.imageUrl(), found.imageUrl());
        assertEquals(sampleItem.description(), found.description());
        assertEquals(0, sampleItem.price().compareTo(found.price()));
        assertEquals(sampleItem.rating(), found.rating());
    }

    @Test
    void deleteById_shouldRemoveJsonFile() {
        repository.save(sampleItem);
        File jsonFile = new File(tempFolder.toFile(), sampleItem.id() + ".json");
        assertTrue(jsonFile.exists());

        repository.deleteById(sampleItem.id());

        assertFalse(jsonFile.exists());
    }

    @Test
    void deleteById_whenFileDoesNotExist_shouldNotThrow() {
        assertDoesNotThrow(() -> repository.deleteById("nonexistent"));
    }

    @Test
    void deleteById_withNullId_shouldNotThrow() {
        assertDoesNotThrow(() -> repository.deleteById(null));
    }

    @Test
    void deleteById_withEmptyId_shouldNotThrow() {
        assertDoesNotThrow(() -> repository.deleteById(""));
    }

    @Test
    void deleteById_shouldMakeItemUnavailable() {
        repository.save(sampleItem);
        assertTrue(repository.findById(sampleItem.id()).isPresent());

        repository.deleteById(sampleItem.id());

        assertTrue(repository.findById(sampleItem.id()).isEmpty());
    }

    @Test
    void repository_shouldHandleMultipleItems() {
        Item item1 = new Item("id-1", "Item 1", "url1", "desc1", BigDecimal.TEN, 4.0);
        Item item2 = new Item("id-2", "Item 2", "url2", "desc2", BigDecimal.valueOf(20), 4.5);
        Item item3 = new Item("id-3", "Item 3", "url3", "desc3", BigDecimal.valueOf(30), 5.0);

        repository.save(item1);
        repository.save(item2);
        repository.save(item3);

        assertTrue(repository.findById("id-1").isPresent());
        assertTrue(repository.findById("id-2").isPresent());
        assertTrue(repository.findById("id-3").isPresent());
    }

    @Test
    void save_withNullOptionalFields_shouldWork() {
        Item itemWithNulls = new Item("id-null", "Name", null, null, BigDecimal.ONE, 3.0);
        
        repository.save(itemWithNulls);
        Optional<Item> found = repository.findById("id-null");

        assertTrue(found.isPresent());
        assertNull(found.get().imageUrl());
        assertNull(found.get().description());
    }

    @Test
    void constructor_shouldCreateStoragePath() {
        String customPath = tempFolder.toString() + File.separator + "custom";
        JsonItemRepository customRepo = new JsonItemRepository(customPath);
        
        Item item = new Item("test-id", "Test", "url", "desc", BigDecimal.TEN, 4.0);
        customRepo.save(item);
        
        File customDir = new File(customPath);
        assertTrue(customDir.exists());
        assertTrue(customDir.isDirectory());
    }
}