package com.tgfcodes.tgfdelivery.courier.management.api.output;

import com.tgfcodes.tgfdelivery.courier.management.domain.model.Courier;

import java.util.Objects;
import java.util.UUID;

public record CourierOutput(
        UUID id,

        String name,

        String phone
) {
    public static CourierOutput toOutput(Courier courier) {
        Objects.requireNonNull(courier, "Courier cannot be null");
        return new CourierOutput(courier.getId(), courier.getName(), courier.getPhone());
    }
}