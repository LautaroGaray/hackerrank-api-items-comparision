package com.items.infraestructure.adapters.inbound.rest;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.items.domain.model.ComparisionResult;
import com.items.domain.model.Item;
import com.items.domain.port.inbound.ComparisionUseCase;
import com.items.domain.port.inbound.CreateItemUseCase;
import com.items.domain.port.inbound.DeleteItemUseCase;
import com.items.domain.port.inbound.GetItemUseCase;
import com.items.domain.port.inbound.UpdateItemUseCase;
import com.items.infraestructure.adapters.inbound.rest.dto.CreateItemRequest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/items")
@Tag(name = "Items", description = "Item management operations")
@SecurityRequirement(name = "JWT")
public class ItemController {

    private final CreateItemUseCase createItemUseCase;
    private final GetItemUseCase getItemUseCase;
    private final UpdateItemUseCase updateItemUseCase;
    private final DeleteItemUseCase deleteItemUseCase;
    private final ComparisionUseCase comparisionUseCase;

    public ItemController(CreateItemUseCase createItemUseCase,
                          GetItemUseCase getItemUseCase,
                          UpdateItemUseCase updateItemUseCase,
                          DeleteItemUseCase deleteItemUseCase,
                          ComparisionUseCase comparisionUseCase) {
        this.createItemUseCase = createItemUseCase;
        this.getItemUseCase = getItemUseCase;
        this.updateItemUseCase = updateItemUseCase;
        this.deleteItemUseCase = deleteItemUseCase;
        this.comparisionUseCase = comparisionUseCase;
    }

   @PostMapping
    @Operation(summary = "Create new item", description = "Creates a new item with auto-generated ID")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Item created successfully with generated ID"),
        @ApiResponse(responseCode = "400", description = "Invalid item data"),
        @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    public Item createItem(@RequestBody CreateItemRequest request){
       
        Item itemWithoutId = new Item(
            request.name(),
            request.imageUrl(),
            request.description(),
            request.price(),
            request.rating()
        );
        
        
        return createItemUseCase.createItem(itemWithoutId);
    }

    @PutMapping
    @Operation(summary = "Update item", description = "Updates an existing item")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Item updated successfully"),
        @ApiResponse(responseCode = "404", description = "Item not found"),
        @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    public Item updateItem(@RequestBody Item item){
        return updateItemUseCase.updateItem(item);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete item", description = "Removes an item by its ID")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Item deleted successfully"),
        @ApiResponse(responseCode = "404", description = "Item not found"),
        @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    public void deleteItem(@PathVariable String id){
        deleteItemUseCase.deleteItem(id);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get item by ID", description = "Retrieves a specific item by its identifier")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Item found"),
        @ApiResponse(responseCode = "404", description = "Item not found"),
        @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    public Item getById(@PathVariable String id){
        return getItemUseCase.getItemById(id);
    }

    @GetMapping("/compare")
    @Operation(summary = "Compare two items", description = "Compares two items and returns the comparison result")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Comparison completed"),
        @ApiResponse(responseCode = "404", description = "One or both items not found"),
        @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    public ComparisionResult compareItems(@RequestParam String id1, @RequestParam String id2) {
        return comparisionUseCase.compare(id1, id2);
    }
    
}
