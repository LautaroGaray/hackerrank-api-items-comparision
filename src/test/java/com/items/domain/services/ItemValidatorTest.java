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

    @Test
    void validate_shouldNotThrowWhenItemIsValid() {
    Item validItem = new Item(
        null,
        "Laptop",
        "http://example.com/laptop.jpg",
        "Gaming laptop",
        BigDecimal.valueOf(1500.0),
        4.5
    );
        assertDoesNotThrow(() -> ItemValidator.validate(validItem));
   }

    @Test
    void validate_shouldThrowWhenItemIsNull() {
        InvalidItemException exception = assertThrows(
            InvalidItemException.class,
            () -> ItemValidator.validate(null)
        );
        assertEquals("Item cannot be null", exception.getMessage());
    }

    @Test
    void validate_shouldThrowWhenNameIsNull() {
        Item itemWithNullName = new Item(
            null,
            null,
            "http://example.com/image.jpg",
            "Description",
            BigDecimal.valueOf(100.0),
            4.5
        );
        assertThrows(InvalidItemException.class, () -> ItemValidator.validate(itemWithNullName));
    }

    @Test
    void validate_shouldThrowWhenNameIsBlank() {
        Item itemWithBlankName = new Item(
            null,
            "   ",
            "http://example.com/image.jpg",
            "Description",
            BigDecimal.valueOf(100.0),
            4.5
        );
        assertThrows(InvalidItemException.class, () -> ItemValidator.validate(itemWithBlankName));
    }

    @Test
    void validate_shouldThrowWhenPriceIsNull() {
        Item itemWithNullPrice = new Item(
            null,
            "Laptop",
            "http://example.com/laptop.jpg",
            "Description",
            null,
            4.5
        );
        assertThrows(InvalidItemException.class, () -> ItemValidator.validate(itemWithNullPrice));
    }

    @Test
    void validate_shouldThrowWhenPriceIsNegative() {
        Item itemWithNegativePrice = new Item(
            null,
            "Laptop",
            "http://example.com/laptop.jpg",
            "Description",
            BigDecimal.valueOf(-100.0),
            4.5
        );
        assertThrows(InvalidItemException.class, () -> ItemValidator.validate(itemWithNegativePrice));
    }

    @Test
    void validate_shouldThrowWhenRatingIsNull() {
        Item itemWithNullRating = new Item(
            null,
            "Laptop",
            "http://example.com/laptop.jpg",
            "Description",
            BigDecimal.valueOf(100.0),
            null
        );
        assertThrows(InvalidItemException.class, () -> ItemValidator.validate(itemWithNullRating));
    }

    @Test
    void validate_shouldThrowWhenRatingIsNegative() {
        Item itemWithNegativeRating = new Item(
            null,
            "Laptop",
            "http://example.com/laptop.jpg",
            "Description",
            BigDecimal.valueOf(100.0),
            -1.0
        );
        assertThrows(InvalidItemException.class, () -> ItemValidator.validate(itemWithNegativeRating));
    }

    @Test
    void validate_shouldThrowWhenRatingIsAboveFive() {
        Item itemWithHighRating = new Item(
            null,
            "Laptop",
            "http://example.com/laptop.jpg",
            "Description",
            BigDecimal.valueOf(100.0),
            5.1
        );
        assertThrows(InvalidItemException.class, () -> ItemValidator.validate(itemWithHighRating));
    }
}