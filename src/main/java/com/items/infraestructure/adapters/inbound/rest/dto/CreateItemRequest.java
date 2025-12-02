package com.items.infraestructure.adapters.inbound.rest.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;

@Schema(description = "Request for creating a new item")
public record CreateItemRequest(
    @Schema(description = "Item name", example = "Laptop")
    String name,
    
    @Schema(description = "Image URL", example = "http://example.com/image.jpg")
    String imageUrl,
    
    @Schema(description = "Item description", example = "Gaming laptop")
    String description,
    
    @Schema(description = "Item price", example = "1500.00")
    BigDecimal price,
    
    @Schema(description = "Item rating", example = "4.5")
    Double rating
) {}