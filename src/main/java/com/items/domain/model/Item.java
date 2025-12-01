package com.items.domain.model;

import java.math.BigDecimal;
import java.util.UUID;

public record Item(UUID id, String name, String imageUrl, String description,
     BigDecimal price, Double rating) {}