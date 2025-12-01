package com.items.infraestructure.adapters.inbound.rest;

import com.items.domain.exception.ItemNotFoundException;
import com.items.domain.model.ComparisionResult;
import com.items.domain.model.Item;
import com.items.domain.port.inbound.ComparisionUseCase;
import com.items.domain.port.inbound.CreateItemUseCase;
import com.items.domain.port.inbound.DeleteItemUseCase;
import com.items.domain.port.inbound.GetItemUseCase;
import com.items.domain.port.inbound.UpdateItemUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ItemControllerTest {

    @Mock
    private CreateItemUseCase createItemUseCase;

    @Mock
    private GetItemUseCase getItemUseCase;

    @Mock
    private UpdateItemUseCase updateItemUseCase;

    @Mock
    private DeleteItemUseCase deleteItemUseCase;

    @Mock
    private ComparisionUseCase comparisionUseCase;

    private ItemController itemController;
    private Item testItem;

    @BeforeEach
    void setUp() {
        itemController = new ItemController(createItemUseCase, getItemUseCase, updateItemUseCase, deleteItemUseCase, comparisionUseCase);
        testItem = new Item("item-123", "Laptop", "http://img.com/laptop.jpg", "Gaming laptop", BigDecimal.valueOf(1500.0), 4.5);
    }

    @Test
    void createItem_shouldReturnCreatedItem() {
        when(createItemUseCase.createItem(testItem)).thenReturn(testItem);

        Item result = itemController.createItem(testItem);

        assertNotNull(result);
        assertEquals("item-123", result.id());
        assertEquals("Laptop", result.name());
        verify(createItemUseCase).createItem(testItem);
    }

    @Test
    void createItem_shouldCallUseCaseOnce() {
        when(createItemUseCase.createItem(testItem)).thenReturn(testItem);

        itemController.createItem(testItem);

        verify(createItemUseCase, times(1)).createItem(testItem);
    }

    @Test
    void getById_shouldReturnItem() {
        when(getItemUseCase.getItemById("item-123")).thenReturn(testItem);

        Item result = itemController.getById("item-123");

        assertNotNull(result);
        assertEquals("item-123", result.id());
        verify(getItemUseCase).getItemById("item-123");
    }

    @Test
    void getById_whenNotFound_shouldThrowException() {
        when(getItemUseCase.getItemById("missing")).thenThrow(new ItemNotFoundException("missing"));

        assertThrows(ItemNotFoundException.class, () -> itemController.getById("missing"));
        verify(getItemUseCase).getItemById("missing");
    }

    @Test
    void updateItem_shouldReturnUpdatedItem() {
        Item updatedItem = new Item("item-123", "Updated Laptop", "http://img.com/laptop.jpg", "Updated desc", BigDecimal.valueOf(1600.0), 4.6);
        when(updateItemUseCase.updateItem(updatedItem)).thenReturn(updatedItem);

        Item result = itemController.updateItem(updatedItem);

        assertNotNull(result);
        assertEquals("Updated Laptop", result.name());
        assertEquals(BigDecimal.valueOf(1600.0), result.price());
        verify(updateItemUseCase).updateItem(updatedItem);
    }

    @Test
    void updateItem_shouldCallUseCaseOnce() {
        when(updateItemUseCase.updateItem(testItem)).thenReturn(testItem);

        itemController.updateItem(testItem);

        verify(updateItemUseCase, times(1)).updateItem(testItem);
    }

    @Test
    void deleteItem_shouldCallDeleteUseCase() {
        doNothing().when(deleteItemUseCase).deleteItem("item-123");

        itemController.deleteItem("item-123");

        verify(deleteItemUseCase).deleteItem("item-123");
    }

    @Test
    void deleteItem_shouldCallUseCaseOnce() {
        doNothing().when(deleteItemUseCase).deleteItem("item-123");

        itemController.deleteItem("item-123");

        verify(deleteItemUseCase, times(1)).deleteItem("item-123");
    }

    @Test
    void compareItems_shouldReturnComparisionResult() {
        ComparisionResult compResult = new ComparisionResult(
            "item-456", 50.0, "item-456", 4.8, 
            Map.of("priceDifference", "1450.00", "ratingDifference", "0.3")
        );
        
        when(comparisionUseCase.compare("item-123", "item-456")).thenReturn(compResult);

        ComparisionResult result = itemController.compareItems("item-123", "item-456");

        assertNotNull(result);
        assertEquals("item-456", result.bestPriceItemId());
        assertEquals(50.0, result.bestPrice());
        assertEquals("item-456", result.bestRankedItem());
        assertEquals(4.8, result.bestRating());
        verify(comparisionUseCase).compare("item-123", "item-456");
    }

    @Test
    void compareItems_whenFirstItemNotFound_shouldThrowException() {
        when(comparisionUseCase.compare("missing", "item-456")).thenThrow(new ItemNotFoundException("missing"));

        assertThrows(ItemNotFoundException.class, () -> itemController.compareItems("missing", "item-456"));
        verify(comparisionUseCase).compare("missing", "item-456");
    }

    @Test
    void compareItems_whenSecondItemNotFound_shouldThrowException() {
        when(comparisionUseCase.compare("item-123", "missing")).thenThrow(new ItemNotFoundException("missing"));

        assertThrows(ItemNotFoundException.class, () -> itemController.compareItems("item-123", "missing"));
    }

    @Test
    void compareItems_shouldCallUseCaseOnce() {
        ComparisionResult compResult = new ComparisionResult("id1", 100.0, "id2", 4.5, Map.of());
        when(comparisionUseCase.compare("item-123", "item-456")).thenReturn(compResult);

        itemController.compareItems("item-123", "item-456");

        verify(comparisionUseCase, times(1)).compare("item-123", "item-456");
    }
}