package com.items.domain.services;

import com.items.domain.exception.InvalidItemException;
import com.items.domain.model.Specification;

public class SpecificationValidator {

    public static void validate(Specification specification) {
        if (specification == null) {
            return;
        }

        validateBrand(specification.brand());
        validateWeight(specification.weight());
        validateWarrantyMonths(specification.warrantyMonths());
    }

    private static void validateBrand(String brand) {
        if (brand != null && brand.isBlank()) {
            throw new InvalidItemException("Brand cannot be blank");
        }
    }

    private static void validateWeight(Double weight) {
        if (weight != null && weight < 0) {
            throw new InvalidItemException("Weight must be greater than or equal to zero");
        }
    }

    private static void validateWarrantyMonths(Integer warrantyMonths) {
        if (warrantyMonths != null && warrantyMonths < 0) {
            throw new InvalidItemException("Warranty months must be greater than or equal to zero");
        }
    }
}