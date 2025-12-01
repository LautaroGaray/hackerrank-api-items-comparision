package com.items.domain.port.inbound;

import com.items.domain.exception.ItemNotFoundException;
import com.items.domain.model.Item;

public interface GetItemUseCase {
    Item getItemById(String id) throws ItemNotFoundException;
}
