package com.items.domain.model;

public record Specification(
    String brand,
    String model,
    String color,
    Double weight,
    String dimensions,
    String material,
    Integer warrantyMonths
) {
    public Specification(String brand, String model, String color, Double weight, String dimensions, String material, Integer warrantyMonths) {
        this.brand = brand;
        this.model = model;
        this.color = color;
        this.weight = weight;
        this.dimensions = dimensions;
        this.material = material;
        this.warrantyMonths = warrantyMonths;
    }
}