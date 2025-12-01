package com.items.domain.port.outbound;

import java.util.Optional;


import com.items.domain.model.Item;

public interface ItemRepositoryPort {    
    Item save(Item item);
    Optional<Item> findById(String id);   
    void deleteById(String id);
}