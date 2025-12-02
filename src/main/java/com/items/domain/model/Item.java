package com.items.domain.model;

import java.math.BigDecimal;

public record Item(String id, String name, String imageUrl, String description,
     BigDecimal price, Double rating) {
         
    public Item(String name, String imageUrl, String description, BigDecimal price, Double rating) {
        this(null, name, imageUrl, description, price, rating);
    }    
   
    public Item withId(String newId) {
        return new Item(newId, this.name, this.imageUrl, this.description, this.price, this.rating);
    }
}