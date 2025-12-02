package com.items.infraestructure.adapters.inbound.rest;

import com.items.domain.exception.ItemNotFoundException;
import com.items.domain.model.ComparisionResult;
import com.items.domain.model.Item;
import com.items.domain.model.Specification;
import com.items.domain.port.inbound.ComparisionUseCase;
import com.items.domain.port.inbound.CreateItemUseCase;
import com.items.domain.port.inbound.DeleteItemUseCase;
import com.items.domain.port.inbound.GetItemUseCase;
import com.items.domain.port.inbound.UpdateItemUseCase;
import com.items.infraestructure.adapters.inbound.rest.dto.CreateItemRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
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
    private Item testItemWithId;
    private CreateItemRequest createRequest;
    private Specification testSpecification;

    @BeforeEach
    void setUp() {
        itemController = new ItemController(createItemUseCase, getItemUseCase, updateItemUseCase, deleteItemUseCase, comparisionUseCase);
        
        testSpecification = new Specification("Dell", "XPS 15", "Silver", 2.5, "357x235x18", "Aluminum", 24);
        
        testItemWithId = new Item(
            "item-123", 
            "Laptop", 
            "http://img.com/laptop.jpg", 
            "Gaming laptop", 
            BigDecimal.valueOf(1500.0), 
            4.5,
            testSpecification
        );
        
        createRequest = new CreateItemRequest(
            "Laptop",
            "http://img.com/laptop.jpg",
            "Gaming laptop",
            BigDecimal.valueOf(1500.0),
            4.5,
            testSpecification
        );
    }

    @Test
    void createItem_shouldReturnItemWithGeneratedId() {
        ArgumentCaptor<Item> itemCaptor = ArgumentCaptor.forClass(Item.class);
        when(createItemUseCase.createItem(any(Item.class))).thenReturn(testItemWithId);

        Item result = itemController.createItem(createRequest);
        verify(createItemUseCase).createItem(itemCaptor.capture());
        
        Item capturedItem = itemCaptor.getValue();
        assertNull(capturedItem.id(), "Item passed to use case should not have id");
        assertEquals("Laptop", capturedItem.name());
        assertEquals(BigDecimal.valueOf(1500.0), capturedItem.price());
        assertEquals(testSpecification, capturedItem.specification());
        
        assertNotNull(result);
        assertNotNull(result.id(), "Returned item should have generated id");
        assertEquals("item-123", result.id());
        assertEquals("Laptop", result.name());
        assertEquals(testSpecification, result.specification());
    }

    @Test
    void createItem_shouldPassItemWithoutIdToUseCase() {
        when(createItemUseCase.createItem(any(Item.class))).thenReturn(testItemWithId);

        itemController.createItem(createRequest);

        ArgumentCaptor<Item> itemCaptor = ArgumentCaptor.forClass(Item.class);
        verify(createItemUseCase).createItem(itemCaptor.capture());
        
        Item capturedItem = itemCaptor.getValue();
        assertNull(capturedItem.id());
        assertEquals("Laptop", capturedItem.name());
        assertEquals("http://img.com/laptop.jpg", capturedItem.imageUrl());
        assertEquals("Gaming laptop", capturedItem.description());
        assertEquals(BigDecimal.valueOf(1500.0), capturedItem.price());
        assertEquals(4.5, capturedItem.rating());
        assertEquals(testSpecification, capturedItem.specification());
    }

    @Test
    void createItem_shouldCallUseCaseOnce() {
        when(createItemUseCase.createItem(any(Item.class))).thenReturn(testItemWithId);

        itemController.createItem(createRequest);

        verify(createItemUseCase, times(1)).createItem(any(Item.class));
    }

    @Test
    void createItem_shouldConvertDtoToItem() {
        when(createItemUseCase.createItem(any(Item.class))).thenReturn(testItemWithId);

        Item result = itemController.createItem(createRequest);
        
        assertEquals(createRequest.name(), result.name());
        assertEquals(createRequest.imageUrl(), result.imageUrl());
        assertEquals(createRequest.description(), result.description());
        assertEquals(createRequest.price(), result.price());
        assertEquals(createRequest.rating(), result.rating());
        assertEquals(createRequest.specification(), result.specification());
    }

    @Test
    void createItem_withNullSpecification_shouldCreateItem() {
        CreateItemRequest requestWithoutSpec = new CreateItemRequest(
            "Mouse",
            "http://img.com/mouse.jpg",
            "Gaming mouse",
            BigDecimal.valueOf(50.0),
            4.8,
            null
        );
        
        Item itemWithoutSpec = new Item(
            "item-456",
            "Mouse",
            "http://img.com/mouse.jpg",
            "Gaming mouse",
            BigDecimal.valueOf(50.0),
            4.8,
            null
        );
        
        when(createItemUseCase.createItem(any(Item.class))).thenReturn(itemWithoutSpec);

        Item result = itemController.createItem(requestWithoutSpec);

        assertNotNull(result);
        assertNull(result.specification());
        verify(createItemUseCase, times(1)).createItem(any(Item.class));
    }

    @Test
    void createItem_withSpecification_shouldIncludeSpecification() {
        ArgumentCaptor<Item> itemCaptor = ArgumentCaptor.forClass(Item.class);
        when(createItemUseCase.createItem(any(Item.class))).thenReturn(testItemWithId);

        itemController.createItem(createRequest);

        verify(createItemUseCase).createItem(itemCaptor.capture());
        Item capturedItem = itemCaptor.getValue();
        
        assertNotNull(capturedItem.specification());
        assertEquals("Dell", capturedItem.specification().brand());
        assertEquals("XPS 15", capturedItem.specification().model());
    }

    @Test
    void getById_shouldReturnItem() {
        when(getItemUseCase.getItemById("item-123")).thenReturn(testItemWithId);

        Item result = itemController.getById("item-123");

        assertNotNull(result);
        assertEquals("item-123", result.id());
        assertEquals(testSpecification, result.specification());
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
        Specification updatedSpec = new Specification("Dell", "XPS 17", "Black", 2.8, "400x300x20", "Aluminum", 36);
        Item itemToUpdate = new Item(
            "item-123", 
            "Updated Laptop", 
            "http://img.com/laptop.jpg", 
            "Updated desc", 
            BigDecimal.valueOf(1600.0), 
            4.6,
            updatedSpec
        );
        
        when(updateItemUseCase.updateItem(itemToUpdate)).thenReturn(itemToUpdate);

        Item result = itemController.updateItem(itemToUpdate);

        assertNotNull(result);
        assertEquals("item-123", result.id());
        assertEquals("Updated Laptop", result.name());
        assertEquals(BigDecimal.valueOf(1600.0), result.price());
        assertEquals(updatedSpec, result.specification());
        verify(updateItemUseCase).updateItem(itemToUpdate);
    }

    @Test
    void updateItem_shouldRequireIdInItem() {
        Item itemWithId = new Item(
            "item-123",
            "Laptop",
            "http://img.com/laptop.jpg",
            "Gaming laptop",
            BigDecimal.valueOf(1500.0),
            4.5,
            testSpecification
        );
        
        when(updateItemUseCase.updateItem(itemWithId)).thenReturn(itemWithId);

        itemController.updateItem(itemWithId);

        ArgumentCaptor<Item> itemCaptor = ArgumentCaptor.forClass(Item.class);
        verify(updateItemUseCase).updateItem(itemCaptor.capture());
        
        Item capturedItem = itemCaptor.getValue();
        assertNotNull(capturedItem.id(), "Update requires item with id");
        assertEquals("item-123", capturedItem.id());
    }

    @Test
    void updateItem_shouldCallUseCaseOnce() {
        when(updateItemUseCase.updateItem(testItemWithId)).thenReturn(testItemWithId);

        itemController.updateItem(testItemWithId);

        verify(updateItemUseCase, times(1)).updateItem(testItemWithId);
    }

    @Test
    void updateItem_withNullSpecification_shouldUpdate() {
        Item itemWithoutSpec = new Item(
            "item-123",
            "Keyboard",
            "http://img.com/kb.jpg",
            "Mechanical keyboard",
            BigDecimal.valueOf(100.0),
            4.7,
            null
        );
        
        when(updateItemUseCase.updateItem(itemWithoutSpec)).thenReturn(itemWithoutSpec);

        Item result = itemController.updateItem(itemWithoutSpec);

        assertNotNull(result);
        assertNull(result.specification());
        verify(updateItemUseCase).updateItem(itemWithoutSpec);
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