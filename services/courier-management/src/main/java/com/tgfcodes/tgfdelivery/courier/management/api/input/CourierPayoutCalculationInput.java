package com.tgfcodes.tgfdelivery.courier.management.api.input;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

public record CourierPayoutCalculationInput(

        @NotNull(message = "Distance in KM is required")
        @DecimalMin(value = "0.0", inclusive = false, message = "Distance in KM must be greater than 0")
        @DecimalMax(value = "10000.0", message = "Distance in KM must be less than or equal to 10000")
        Double distanceInKm
) {
}