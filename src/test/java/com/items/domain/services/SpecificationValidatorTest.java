package com.items.domain.services;

import com.items.domain.exception.InvalidItemException;
import com.items.domain.model.Specification;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class SpecificationValidatorTest {

    @Test
    void validate_shouldNotThrowWhenSpecificationIsNull() {
        assertDoesNotThrow(() -> SpecificationValidator.validate(null));
    }

    @Test
    void validate_shouldNotThrowWhenAllFieldsAreValid() {
        Specification spec = new Specification(
            "Dell",
            "XPS 15",
            "Silver",
            2.5,
            "357 x 235 x 18 mm",
            "Aluminum",
            24
        );

        assertDoesNotThrow(() -> SpecificationValidator.validate(spec));
    }

    @Test
    void validate_shouldNotThrowWhenAllFieldsAreNull() {
        Specification spec = new Specification(null, null, null, null, null, null, null);
        assertDoesNotThrow(() -> SpecificationValidator.validate(spec));
    }

    @Test
    void validate_shouldThrowWhenBrandIsBlank() {
        Specification spec = new Specification("   ", "Model", "Color", 2.0, "dimensions", "material", 12);
        
        InvalidItemException exception = assertThrows(
            InvalidItemException.class,
            () -> SpecificationValidator.validate(spec)
        );
        
        assertEquals("Brand cannot be blank", exception.getMessage());
    }

    @Test
    void validate_shouldThrowWhenBrandIsEmpty() {
        Specification spec = new Specification("", "Model", "Color", 2.0, "dimensions", "material", 12);
        assertThrows(InvalidItemException.class, () -> SpecificationValidator.validate(spec));
    }

    @Test
    void validate_shouldNotThrowWhenBrandIsNull() {
        Specification spec = new Specification(null, "Model", "Color", 2.0, "dimensions", "material", 12);
        assertDoesNotThrow(() -> SpecificationValidator.validate(spec));
    }

    @Test
    void validate_shouldThrowWhenWeightIsNegative() {
        Specification spec = new Specification("Brand", "Model", "Color", -1.0, "dimensions", "material", 12);
        
        InvalidItemException exception = assertThrows(
            InvalidItemException.class,
            () -> SpecificationValidator.validate(spec)
        );
        
        assertEquals("Weight must be greater than or equal to zero", exception.getMessage());
    }

    @Test
    void validate_shouldNotThrowWhenWeightIsZero() {
        Specification spec = new Specification("Brand", "Model", "Color", 0.0, "dimensions", "material", 12);
        assertDoesNotThrow(() -> SpecificationValidator.validate(spec));
    }

    @Test
    void validate_shouldNotThrowWhenWeightIsNull() {
        Specification spec = new Specification("Brand", "Model", "Color", null, "dimensions", "material", 12);
        assertDoesNotThrow(() -> SpecificationValidator.validate(spec));
    }

    @Test
    void validate_shouldThrowWhenWarrantyMonthsIsNegative() {
        Specification spec = new Specification("Brand", "Model", "Color", 2.0, "dimensions", "material", -1);
        
        InvalidItemException exception = assertThrows(
            InvalidItemException.class,
            () -> SpecificationValidator.validate(spec)
        );
        
        assertEquals("Warranty months must be greater than or equal to zero", exception.getMessage());
    }

    @Test
    void validate_shouldNotThrowWhenWarrantyMonthsIsZero() {
        Specification spec = new Specification("Brand", "Model", "Color", 2.0, "dimensions", "material", 0);
        assertDoesNotThrow(() -> SpecificationValidator.validate(spec));
    }

    @Test
    void validate_shouldNotThrowWhenWarrantyMonthsIsNull() {
        Specification spec = new Specification("Brand", "Model", "Color", 2.0, "dimensions", "material", null);
        assertDoesNotThrow(() -> SpecificationValidator.validate(spec));
    }

    @Test
    void validate_shouldNotThrowWhenModelIsNull() {
        Specification spec = new Specification("Brand", null, "Color", 2.0, "dimensions", "material", 12);
        assertDoesNotThrow(() -> SpecificationValidator.validate(spec));
    }

    @Test
    void validate_shouldNotThrowWhenColorIsNull() {
        Specification spec = new Specification("Brand", "Model", null, 2.0, "dimensions", "material", 12);
        assertDoesNotThrow(() -> SpecificationValidator.validate(spec));
    }

    @Test
    void validate_shouldNotThrowWhenDimensionsIsNull() {
        Specification spec = new Specification("Brand", "Model", "Color", 2.0, null, "material", 12);
        assertDoesNotThrow(() -> SpecificationValidator.validate(spec));
    }

    @Test
    void validate_shouldNotThrowWhenMaterialIsNull() {
        Specification spec = new Specification("Brand", "Model", "Color", 2.0, "dimensions", null, 12);
        assertDoesNotThrow(() -> SpecificationValidator.validate(spec));
    }
}