package com.items.application.service;

import com.items.domain.exception.InvalidItemException;
import com.items.domain.exception.ItemNotFoundException;
import com.items.domain.model.ComparisionResult;
import com.items.domain.model.Item;
import com.items.domain.port.outbound.ItemRepositoryPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ItemServiceTest {

    @Mock
    private ItemRepositoryPort itemRepository;

    @InjectMocks
    private ItemService itemService;

    private Item validItem;
    private Item validItem2;

    @BeforeEach
    void setUp() {
        validItem = new Item("id1", "Item A", "http://example.com/a.jpg", "Description A", BigDecimal.valueOf(100.0), 4.2);
        validItem2 = new Item("id2", "Item B", "http://example.com/b.jpg", "Description B", BigDecimal.valueOf(150.0), 4.8);
    }

   @Test
    void createItem_shouldGenerateIdAndSaveItem() {
        Item itemWithoutId = new Item(
            null,
            "Laptop",
            "http://example.com/laptop.jpg",
            "Gaming laptop",
            BigDecimal.valueOf(1500.0),
            4.5
        );
        
        Item itemWithId = new Item(
            "generated-uuid",
            "Laptop",
            "http://example.com/laptop.jpg",
            "Gaming laptop",
            BigDecimal.valueOf(1500.0),
            4.5
        );
        
        when(itemRepository.save(any(Item.class))).thenReturn(itemWithId);

        Item result = itemService.createItem(itemWithoutId);

        assertNotNull(result);
        assertNotNull(result.id(), "Generated ID should not be null");
        assertEquals("Laptop", result.name());
        assertEquals(BigDecimal.valueOf(1500.0), result.price());
        
        verify(itemRepository, times(1)).save(any(Item.class));
    }

    @Test
    void createItem_shouldGenerateUniqueIdForEachItem() {
        Item item1WithoutId = new Item(null, "Item 1", "http://url1", "desc1", BigDecimal.TEN, 4.0);
        Item item2WithoutId = new Item(null, "Item 2", "http://url2", "desc2", BigDecimal.TEN, 4.0);
        
        Item item1WithId = new Item("uuid-1", "Item 1", "http://url1", "desc1", BigDecimal.TEN, 4.0);
        Item item2WithId = new Item("uuid-2", "Item 2", "http://url2", "desc2", BigDecimal.TEN, 4.0);
        
        when(itemRepository.save(any(Item.class)))
            .thenReturn(item1WithId)
            .thenReturn(item2WithId);

        Item result1 = itemService.createItem(item1WithoutId);
        Item result2 = itemService.createItem(item2WithoutId);

        assertNotNull(result1.id());
        assertNotNull(result2.id());
        assertNotEquals(result1.id(), result2.id(), "IDs should be unique");
        
        verify(itemRepository, times(2)).save(any(Item.class));
    }

    @Test
    void createItem_shouldAcceptItemWithoutId() {
        Item itemWithoutId = new Item(
            null,
            "Mouse",
            "http://example.com/mouse.jpg",
            "Gaming mouse",
            BigDecimal.valueOf(50.0),
            4.8
        );
        
        Item itemWithId = itemWithoutId.withId("auto-generated-id");
        
        when(itemRepository.save(any(Item.class))).thenReturn(itemWithId);

        Item result = itemService.createItem(itemWithoutId);

        assertNotNull(result.id());
        verify(itemRepository, times(1)).save(any(Item.class));
    }

    @Test
    void createItem_shouldPassItemWithGeneratedIdToRepository() {
        Item itemWithoutId = new Item(null, "Keyboard", "http://url", "desc", BigDecimal.valueOf(100.0), 4.5);
        
        ArgumentCaptor<Item> itemCaptor = ArgumentCaptor.forClass(Item.class);
        when(itemRepository.save(any(Item.class))).thenAnswer(invocation -> invocation.getArgument(0));

        itemService.createItem(itemWithoutId);

        verify(itemRepository).save(itemCaptor.capture());
        Item savedItem = itemCaptor.getValue();
        
        assertNotNull(savedItem.id(), "Item saved to repository should have generated ID");
        assertEquals("Keyboard", savedItem.name());
    }

    @Test
    void createItem_shouldThrowWhenItemIsNull() {
        assertThrows(InvalidItemException.class, () -> itemService.createItem(null));
        verify(itemRepository, never()).save(any());
    }

    @Test
    void createItem_shouldThrowWhenNameIsNull() {
        Item invalidItem = new Item(null, null, "url", "desc", BigDecimal.TEN, 4.0);
        assertThrows(InvalidItemException.class, () -> itemService.createItem(invalidItem));
        verify(itemRepository, never()).save(any());
    }

    @Test
    void createItem_shouldThrowWhenPriceIsNegative() {
        Item invalidItem = new Item(null, "name", "url", "desc", BigDecimal.valueOf(-10), 4.0);
        assertThrows(InvalidItemException.class, () -> itemService.createItem(invalidItem));
        verify(itemRepository, never()).save(any());
    }

    @Test
    void createItem_shouldThrowWhenRatingIsInvalid() {
        Item invalidItem = new Item(null, "name", "url", "desc", BigDecimal.TEN, 6.0);
        assertThrows(InvalidItemException.class, () -> itemService.createItem(invalidItem));
        verify(itemRepository, never()).save(any());
    }

    @Test
    void createItem_shouldValidateBeforeGeneratingId() {
        Item invalidItem = new Item(null, "", "url", "desc", BigDecimal.valueOf(-5), 10.0);
        
        assertThrows(InvalidItemException.class, () -> itemService.createItem(invalidItem));
        verify(itemRepository, never()).save(any());
    }

    @Test
    void getItemById_shouldReturnItemWhenExists() {
        when(itemRepository.findById("id1")).thenReturn(Optional.of(validItem));

        Item result = itemService.getItemById("id1");

        assertNotNull(result);
        assertEquals("id1", result.id());
        verify(itemRepository, times(1)).findById("id1");
    }

    @Test
    void getItemById_shouldThrowWhenItemNotFound() {
        when(itemRepository.findById("id999")).thenReturn(Optional.empty());

        assertThrows(ItemNotFoundException.class, () -> itemService.getItemById("id999"));
        verify(itemRepository, times(1)).findById("id999");
    }

    @Test
    void getItemById_shouldThrowWhenIdIsNull() {
        assertThrows(InvalidItemException.class, () -> itemService.getItemById(null));
        verify(itemRepository, never()).findById(any());
    }

    @Test
    void updateItem_shouldUpdateAndReturnItem() {
        when(itemRepository.findById("id1")).thenReturn(Optional.of(validItem));
        when(itemRepository.save(any(Item.class))).thenReturn(validItem);

        Item result = itemService.updateItem(validItem);

        assertNotNull(result);
        assertEquals(validItem.id(), result.id());
        verify(itemRepository, times(1)).findById("id1");
        verify(itemRepository, times(1)).save(validItem);
    }

    @Test
    void updateItem_shouldThrowWhenItemNotFound() {
        when(itemRepository.findById("id1")).thenReturn(Optional.empty());

        assertThrows(ItemNotFoundException.class, () -> itemService.updateItem(validItem));
        verify(itemRepository, times(1)).findById("id1");
        verify(itemRepository, never()).save(any());
    }

    @Test
    void updateItem_shouldThrowWhenItemIsNull() {
        assertThrows(InvalidItemException.class, () -> itemService.updateItem(null));
        verify(itemRepository, never()).findById(any());
        verify(itemRepository, never()).save(any());
    }

    @Test
    void updateItem_shouldThrowWhenIdIsNull() {
        Item invalidItem = new Item(null, "name", "url", "desc", BigDecimal.TEN, 4.0);
        assertThrows(InvalidItemException.class, () -> itemService.updateItem(invalidItem));
        verify(itemRepository, never()).save(any());
    }

    @Test
    void deleteItem_shouldDeleteWhenItemExists() {
        when(itemRepository.findById("id1")).thenReturn(Optional.of(validItem));
        doNothing().when(itemRepository).deleteById("id1");

        itemService.deleteItem("id1");

        verify(itemRepository, times(1)).findById("id1");
        verify(itemRepository, times(1)).deleteById("id1");
    }

    @Test
    void deleteItem_shouldThrowWhenItemNotFound() {
        when(itemRepository.findById("id999")).thenReturn(Optional.empty());

        assertThrows(ItemNotFoundException.class, () -> itemService.deleteItem("id999"));
        verify(itemRepository, times(1)).findById("id999");
        verify(itemRepository, never()).deleteById(any());
    }

    @Test
    void deleteItem_shouldThrowWhenIdIsNull() {
        assertThrows(InvalidItemException.class, () -> itemService.deleteItem(null));
        verify(itemRepository, never()).findById(any());
        verify(itemRepository, never()).deleteById(any());
    }

    @Test
    void compare_shouldReturnComparisionResult() {
        when(itemRepository.findById("id1")).thenReturn(Optional.of(validItem));
        when(itemRepository.findById("id2")).thenReturn(Optional.of(validItem2));

        ComparisionResult result = itemService.compare("id1", "id2");

        assertNotNull(result);
        assertEquals("id1", result.bestPriceItemId());
        assertEquals(100.0, result.bestPrice());
        assertEquals("id2", result.bestRankedItem());
        assertEquals(4.8, result.bestRating());
        assertNotNull(result.differences());
        verify(itemRepository, times(1)).findById("id1");
        verify(itemRepository, times(1)).findById("id2");
    }

    @Test
    void compare_shouldThrowWhenFirstItemNotFound() {
        when(itemRepository.findById("id1")).thenReturn(Optional.empty());

        assertThrows(ItemNotFoundException.class, () -> itemService.compare("id1", "id2"));
        verify(itemRepository, times(1)).findById("id1");
        verify(itemRepository, never()).findById("id2");
    }

    @Test
    void compare_shouldThrowWhenSecondItemNotFound() {
        when(itemRepository.findById("id1")).thenReturn(Optional.of(validItem));
        when(itemRepository.findById("id2")).thenReturn(Optional.empty());

        assertThrows(ItemNotFoundException.class, () -> itemService.compare("id1", "id2"));
        verify(itemRepository, times(1)).findById("id1");
        verify(itemRepository, times(1)).findById("id2");
    }

    @Test
    void compare_shouldThrowWhenId1IsNull() {
        assertThrows(InvalidItemException.class, () -> itemService.compare(null, "id2"));
        verify(itemRepository, never()).findById(any());
    }

    @Test
    void compare_shouldThrowWhenId2IsNull() {
        assertThrows(InvalidItemException.class, () -> itemService.compare("id1", null));
        verify(itemRepository, never()).findById(any());
    }
}