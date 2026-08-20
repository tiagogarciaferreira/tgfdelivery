package com.tgfcodes.tgfdelivery.delivery.tracking.api.input;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.List;

public record DeliveryInput(

        @Valid
        @NotNull(message = "Sender is required")
        ContactPointInput sender,

        @Valid
        @NotNull(message = "Recipient is required")
        ContactPointInput recipient,

        @NotNull(message = "Items are required")
        @Size(min = 1, max = 50, message = "You can add up to 50 items per delivery")
        List<@Valid @NotNull(message = "Item is required") AddItemInput> items
) {
}