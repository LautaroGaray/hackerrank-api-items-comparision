package com.items.domain.port.inbound;

import com.items.domain.model.Item;

public interface CreateItemUseCase {
    Item createItem(Item item);
}
