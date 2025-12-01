package com.items.application.service;

import java.math.BigDecimal;
import java.util.Map;

import com.items.domain.exception.InvalidItemException;
import com.items.domain.exception.ItemNotFoundException;
import com.items.domain.model.ComparisionResult;
import com.items.domain.model.Item;
import com.items.domain.port.inbound.ComparisionUseCase;
import com.items.domain.port.inbound.CreateItemUseCase;
import com.items.domain.port.inbound.DeleteItemUseCase;
import com.items.domain.port.inbound.GetItemUseCase;
import com.items.domain.port.inbound.UpdateItemUseCase;
import com.items.domain.port.outbound.ItemRepositoryPort;
import com.items.domain.services.ItemValidator;

public class ItemService implements
                                ComparisionUseCase, 
                                GetItemUseCase,
                                CreateItemUseCase,
                                UpdateItemUseCase,
                                DeleteItemUseCase {
    private final ItemRepositoryPort itemRepository;

    public ItemService(ItemRepositoryPort itemRepository) {
        this.itemRepository = itemRepository;
    }

    @Override
    public ComparisionResult compare(String id1, String id2) throws ItemNotFoundException {
     
        ItemValidator.validateIdNotNull(id1);
        ItemValidator.validateIdNotNull(id2);       
        
        
        Item item1 = itemRepository.findById(id1)
            .orElseThrow(() -> new ItemNotFoundException("Item with id " + id1 + " not found"));
        Item item2 = itemRepository.findById(id2)
            .orElseThrow(() -> new ItemNotFoundException("Item with id " + id2 + " not found"));

        ItemValidator.validatePrice(item1.price());
        ItemValidator.validatePrice(item2.price());
        // Mejor precio (menor)
        String bestPriceItemId = item1.price().compareTo(item2.price()) < 0 ? id1 : id2;
        double bestPrice = item1.price().compareTo(item2.price()) < 0 ? 
            item1.price().doubleValue() : item2.price().doubleValue();

        ItemValidator.validateRating(item1.rating());
        ItemValidator.validateRating(item2.rating());

        // Mejor rating (mayor)
        String bestRatedItem = item1.rating() > item2.rating() ? id1 : id2;
        Double bestRating = Math.max(item1.rating(), item2.rating());        

        // Diferencias 
        BigDecimal priceDifference = item1.price().subtract(item2.price()).abs();
        Double ratingDifference = Math.abs(item1.rating() - item2.rating());

        Map<String, String> differences = Map.of(
            "priceDifference", priceDifference.toString(),
            "ratingDifference", ratingDifference.toString()
        );

        return new ComparisionResult(bestPriceItemId, bestPrice, bestRatedItem, bestRating, differences);      
        
    }

    @Override
    public Item getItemById(String id) throws ItemNotFoundException{
        ItemValidator.validateIdNotNull(id);
        return itemRepository.findById(id)
            .orElseThrow(() -> new ItemNotFoundException("Item with id " + id + " not found"));
    }

    @Override
    public void deleteItem(String id) {
        ItemValidator.validateIdNotNull(id);
        Item itemFromDB = getItemById(id); 
        ItemValidator.validateNotNull(itemFromDB);
        
        itemRepository.deleteById(id);
    }

    @Override
    public Item updateItem(Item item) throws InvalidItemException {
        ItemValidator.validateNotNull(item);
        ItemValidator.validateIdNotNull(item.id());
        Item itemFromDB = getItemById(item.id()); 
        ItemValidator.validateNotNull(itemFromDB);

        return itemRepository.save(item);
    }

    @Override
    public Item createItem(Item item) throws InvalidItemException {
        ItemValidator.validateNotNull(item);
        ItemValidator.validateIdNotNull(item.id());        
        ItemValidator.validateName(item.name());
        ItemValidator.validatePrice(item.price());
        ItemValidator.validateRating(item.rating());
        
        return itemRepository.save(item);
    }
 
}
