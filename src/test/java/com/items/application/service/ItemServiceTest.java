package com.items.application.service;

import com.items.domain.exception.InvalidItemException;
import com.items.domain.exception.ItemNotFoundException;
import com.items.domain.model.ComparisionResult;
import com.items.domain.model.Item;
import com.items.domain.model.Specification;
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
        Specification specification = new Specification("Dell", "XPS 15", "Silver", 2.5, "357x235x18", "Aluminum", 24);        
    
        validItem = new Item("generated-id-123", "Item A", "http://example.com/a.jpg", "Description A", BigDecimal.valueOf(100.0), 4.2, specification);
        validItem2 = new Item("generated-id-456", "Item B", "http://example.com/b.jpg", "Description B", BigDecimal.valueOf(150.0), 4.8, specification);
    }

   @Test
    void createItem_shouldGenerateIdAndSaveItem() {
        
        Specification specification = new Specification("HP", "Pavilion", "Black", 2.0, "320x220x15", "Plastic", 12);
        Item itemWithoutId = new Item(null, "New Item", "http://img.url", "desc", BigDecimal.valueOf(100.0), 4.5, specification);
        
       
        Item itemWithGeneratedId = new Item("auto-generated-uuid", "New Item", "http://img.url", "desc", BigDecimal.valueOf(100.0), 4.5, specification);
        when(itemRepository.save(any(Item.class))).thenReturn(itemWithGeneratedId);

        
        Item result = itemService.createItem(itemWithoutId);

        
        assertNotNull(result);
        assertNotNull(result.id(), "ID should be auto-generated");
        assertEquals("auto-generated-uuid", result.id());
        assertEquals("New Item", result.name());
        
        
        ArgumentCaptor<Item> itemCaptor = ArgumentCaptor.forClass(Item.class);
        verify(itemRepository, times(1)).save(itemCaptor.capture());
        
        Item savedItem = itemCaptor.getValue();
        assertNotNull(savedItem.id(), "Saved item should have generated ID");
    }
   
    @Test
    void createItem_shouldThrowWhenItemIsNull() {
        assertThrows(InvalidItemException.class, () -> itemService.createItem(null));
        verify(itemRepository, never()).save(any());
    }

    @Test
    void createItem_shouldNotThrowWhenIdIsNull() {
        
        Specification specification = new Specification("spec1", "Spec Description", null, null, null, null, null);
        Item itemWithoutId = new Item(null, "name", "url", "desc", BigDecimal.TEN, 4.0, specification);
        
        Item itemWithId = new Item("generated-uuid", "name", "url", "desc", BigDecimal.TEN, 4.0, specification);
        when(itemRepository.save(any(Item.class))).thenReturn(itemWithId);
        
       
        assertDoesNotThrow(() -> itemService.createItem(itemWithoutId));
        verify(itemRepository, times(1)).save(any(Item.class));
    }

    @Test
    void createItem_shouldThrowWhenNameIsNull() {
        Specification specification = new Specification("spec1", "Spec Description", null, null, null, null, null);
        Item invalidItem = new Item(null, null, "url", "desc", BigDecimal.TEN, 4.0, specification);
        assertThrows(InvalidItemException.class, () -> itemService.createItem(invalidItem));
        verify(itemRepository, never()).save(any());
    }

    @Test
    void createItem_shouldThrowWhenPriceIsNegative() {
        Specification specification = new Specification("spec1", "Spec Description", null, null, null, null, null);
        Item invalidItem = new Item(null, "name", "url", "desc", BigDecimal.valueOf(-10), 4.0, specification);
        assertThrows(InvalidItemException.class, () -> itemService.createItem(invalidItem));
        verify(itemRepository, never()).save(any());
    }

    @Test
    void createItem_shouldThrowWhenRatingIsInvalid() {
        Specification specification = new Specification("spec1", "Spec Description", null, null, null, null, null);
        Item invalidItem = new Item(null, "name", "url", "desc", BigDecimal.TEN, 6.0, specification);
        assertThrows(InvalidItemException.class, () -> itemService.createItem(invalidItem));
        verify(itemRepository, never()).save(any());
    }

    @Test
    void createItem_shouldValidateSpecification() {
       
        Specification invalidSpec = new Specification("   ", "Model", "Color", 2.0, "dims", "material", 12);
        Item itemWithInvalidSpec = new Item(null, "Item", "url", "desc", BigDecimal.TEN, 4.0, invalidSpec);
        
        assertThrows(InvalidItemException.class, () -> itemService.createItem(itemWithInvalidSpec));
        verify(itemRepository, never()).save(any());
    }

    @Test
    void createItem_shouldAcceptNullSpecification() {
        Item itemWithoutSpec = new Item(null, "Item", "url", "desc", BigDecimal.TEN, 4.0, null);
        Item savedItem = new Item("generated-id", "Item", "url", "desc", BigDecimal.TEN, 4.0, null);
        
        when(itemRepository.save(any(Item.class))).thenReturn(savedItem);
        
        Item result = itemService.createItem(itemWithoutSpec);
        
        assertNotNull(result);
        assertNotNull(result.id());
        assertNull(result.specification());
        verify(itemRepository, times(1)).save(any(Item.class));
    }

    @Test
    void updateItem_shouldRequireId() {
       
        Specification specification = new Specification("spec1", "Spec Description", null, null, null, null, null);
        Item itemWithoutId = new Item(null, "name", "url", "desc", BigDecimal.TEN, 4.0, specification);
        
        assertThrows(InvalidItemException.class, () -> itemService.updateItem(itemWithoutId));
        verify(itemRepository, never()).save(any());
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