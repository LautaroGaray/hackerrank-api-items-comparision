package com.items.domain.services;

import com.items.domain.exception.InvalidItemException;
import com.items.domain.model.Item;
import org.junit.jupiter.api.Test;
import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class ItemValidatorTest {

    @Test
    void validateNotNull_shouldThrowWhenItemIsNull() {
        assertThrows(InvalidItemException.class, () -> ItemValidator.validateNotNull(null));
    }

    @Test
    void validateNotNull_shouldNotThrowWhenItemIsValid() {
        Item item = new Item("id1", "name", "url", "desc", BigDecimal.TEN, 4.0);
        assertDoesNotThrow(() -> ItemValidator.validateNotNull(item));
    }

    @Test
    void validateIdNotNull_shouldThrowWhenIdIsNull() {
        assertThrows(InvalidItemException.class, () -> ItemValidator.validateIdNotNull(null));
    }

    @Test
    void validateIdNotNull_shouldThrowWhenIdIsBlank() {
        assertThrows(InvalidItemException.class, () -> ItemValidator.validateIdNotNull(""));
        assertThrows(InvalidItemException.class, () -> ItemValidator.validateIdNotNull("   "));
    }

    @Test
    void validateIdNotNull_shouldNotThrowWhenIdIsValid() {
        assertDoesNotThrow(() -> ItemValidator.validateIdNotNull("valid-id"));
    }

    @Test
    void validatePrice_shouldThrowWhenPriceIsNull() {
        assertThrows(InvalidItemException.class, () -> ItemValidator.validatePrice(null));
    }

    @Test
    void validatePrice_shouldThrowWhenPriceIsNegative() {
        assertThrows(InvalidItemException.class, () -> ItemValidator.validatePrice(BigDecimal.valueOf(-1)));
    }

    @Test
    void validatePrice_shouldNotThrowWhenPriceIsZero() {
        assertDoesNotThrow(() -> ItemValidator.validatePrice(BigDecimal.ZERO));
    }

    @Test
    void validatePrice_shouldNotThrowWhenPriceIsPositive() {
        assertDoesNotThrow(() -> ItemValidator.validatePrice(BigDecimal.TEN));
    }

    @Test
    void validateRating_shouldThrowWhenRatingIsNull() {
        assertThrows(InvalidItemException.class, () -> ItemValidator.validateRating(null));
    }

    @Test
    void validateRating_shouldThrowWhenRatingIsNegative() {
        assertThrows(InvalidItemException.class, () -> ItemValidator.validateRating(-0.1));
    }

    @Test
    void validateRating_shouldThrowWhenRatingIsAboveFive() {
        assertThrows(InvalidItemException.class, () -> ItemValidator.validateRating(5.1));
    }

    @Test
    void validateRating_shouldNotThrowWhenRatingIsZero() {
        assertDoesNotThrow(() -> ItemValidator.validateRating(0.0));
    }

    @Test
    void validateRating_shouldNotThrowWhenRatingIsFive() {
        assertDoesNotThrow(() -> ItemValidator.validateRating(5.0));
    }

    @Test
    void validateRating_shouldNotThrowWhenRatingIsValid() {
        assertDoesNotThrow(() -> ItemValidator.validateRating(4.5));
    }

    @Test
    void validateName_shouldThrowWhenNameIsNull() {
        assertThrows(InvalidItemException.class, () -> ItemValidator.validateName(null));
    }

    @Test
    void validateName_shouldThrowWhenNameIsBlank() {
        assertThrows(InvalidItemException.class, () -> ItemValidator.validateName(""));
        assertThrows(InvalidItemException.class, () -> ItemValidator.validateName("   "));
    }

    @Test
    void validateName_shouldNotThrowWhenNameIsValid() {
        assertDoesNotThrow(() -> ItemValidator.validateName("Valid Name"));
    }
}