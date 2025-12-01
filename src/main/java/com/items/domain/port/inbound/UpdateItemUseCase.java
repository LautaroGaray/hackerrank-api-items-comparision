package com.items.domain.port.inbound;

import com.items.domain.model.Item;

public interface UpdateItemUseCase {
    Item updateItem(Item item);
}
