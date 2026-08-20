package com.tgfcodes.tgfdelivery.delivery.tracking.api.output;

import com.tgfcodes.tgfdelivery.delivery.tracking.domain.model.Item;

import java.util.Objects;
import java.util.UUID;

public record ItemOutput(
        UUID id,

        String name,

        Integer quantity
) {

    public static ItemOutput toOutput(Item item) {
        Objects.requireNonNull(item, "Item cannot be null");
        return new ItemOutput(item.getId(), item.getName(), item.getQuantity());
    }
}