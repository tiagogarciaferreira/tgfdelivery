package com.tgfcodes.tgfdelivery.delivery.tracking.api.input;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record CourierIdInput(

        @NotNull(message = "Courier ID is required")
        UUID courierId
) {
}