package com.items.infraestructure.mapper;

import com.items.domain.model.Item;
import com.items.infraestructure.entities.ItemEntity;

public class ItemEntityMapper {
    public ItemEntity toEntity(Item item){
        return new ItemEntity(
                item.id(),
                item.name(),
                item.imageUrl(),
                item.description(),
                item.price(),
                item.rating()
        );
    }

    public Item toDomain(ItemEntity itemEntity){
        return new Item(
                itemEntity.getId(),
                itemEntity.getName(),
                itemEntity.getImageUrl(),
                itemEntity.getDescription(),
                itemEntity.getPrice(),
                itemEntity.getRating()
        );
    }
}
