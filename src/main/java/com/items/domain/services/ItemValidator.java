package com.items.domain.services;

import com.items.domain.exception.InvalidItemException;
import com.items.domain.model.Item;

import java.math.BigDecimal;


public final class ItemValidator {

    public static void validate(Item item) {
        if (item == null) {
            throw new InvalidItemException("Item cannot be null");
        }
        
        if (item.name() == null || item.name().isBlank()) {
            throw new InvalidItemException("Item name is required");
        }
        
        validatePrice(item.price());
        validateRating(item.rating());

        SpecificationValidator.validate(item.specification());
    }
    
   public static void validateNotNull(Item item) {
        if (item == null) {
            throw new InvalidItemException("Item cannot be null");
        }
    }

    public static void validateIdNotNull(String id) {
        if (id == null || id.isBlank()) {
            throw new InvalidItemException("Item ID cannot be null or blank");
        }
    }

    public static void validateName(String name) {
        if (name == null || name.isBlank()) {
            throw new InvalidItemException("Item name cannot be null or blank");
        }
    }

    public static void validatePrice(BigDecimal price) {
        if (price == null) {
            throw new InvalidItemException("Item price cannot be null");
        }
        if (price.signum() < 0) {
            throw new InvalidItemException("Item price cannot be negative");
        }
    }

    public static void validateRating(Double rating) {
        if (rating == null) {
            throw new InvalidItemException("Item rating cannot be null");
        }
        if (rating < 0.0 || rating > 5.0) {
            throw new InvalidItemException("Item rating must be between 0.0 and 5.0");
        }
    }   
    
}