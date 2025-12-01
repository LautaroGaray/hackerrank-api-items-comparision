package com.items.domain.model;

import java.math.BigDecimal;

public record Item(String id, String name, String imageUrl, String description,
     BigDecimal price, Double rating) {}