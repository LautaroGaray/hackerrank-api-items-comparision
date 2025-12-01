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

@RestController
@RequestMapping("/items")
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
    public Item createItem(@RequestBody Item item){
        return createItemUseCase.createItem(item);
    }

    @PutMapping
    public Item updateItem(@RequestBody Item item){
        return updateItemUseCase.updateItem(item);
    }

    @DeleteMapping("/{id}")
    public void deleteItem(@PathVariable String id){
        deleteItemUseCase.deleteItem(id);
    }

    @GetMapping("/{id}")
    public Item getById(@PathVariable String id){
        return getItemUseCase.getItemById(id);
    }

    @GetMapping("/compare")
    public ComparisionResult compareItems(@RequestParam String id1, @RequestParam String id2) {
        return comparisionUseCase.compare(id1, id2);
    }
    
}
