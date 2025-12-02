package com.items.domain.model;

import java.math.BigDecimal;

public record Item(String id, String name, String imageUrl, String description,
     BigDecimal price, Double rating, Specification specification) {
         
    public Item(String name, String imageUrl, String description, BigDecimal price, Double rating, Specification specification) {
        this(null, name, imageUrl, description, price, rating, specification);
    }    
   
    public Item withId(String newId) {
        return new Item(newId, this.name, this.imageUrl, this.description, this.price, this.rating, this.specification);
    }
}