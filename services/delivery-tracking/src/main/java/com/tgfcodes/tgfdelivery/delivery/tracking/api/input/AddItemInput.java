package com.tgfcodes.tgfdelivery.delivery.tracking.api.input;

import jakarta.validation.constraints.*;

public record AddItemInput(
        @NotBlank(message = "Item name is required")
        @Size(min = 2, max = 100, message = "Item name must be between 2 and 100 characters")
        String name,

        @NotNull(message = "Quantity is required")
        @Min(value = 1, message = "Quantity must be at least 1")
        @Max(value = 100, message = "Quantity cannot exceed 100 items per line")
        Integer quantity
) {
}