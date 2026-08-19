package com.tgfcodes.tgfdelivery.delivery.tracking.api.input;

import com.tgfcodes.tgfdelivery.delivery.tracking.domain.model.ContactPoint;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record ContactPointInput(
        @NotBlank(message = "Name is required")
        @Size(min = 2, max = 100, message = "Name must be between 2 and 100 characters")
        String name,

        @NotBlank(message = "Phone is required")
        @Pattern(regexp = "^\\+?[1-9]\\d{1,14}$", message = "Phone must be in valid E.164 format")
        String phone,

        @NotBlank(message = "Zip code is required")
        @Size(min = 5, max = 10, message = "Zip code must be between 5 and 10 characters")
        String zipCode,

        @NotBlank(message = "Street is required")
        @Size(min = 2, max = 150, message = "Street must be between 2 and 150 characters")
        String street,

        @NotBlank(message = "Number is required")
        @Size(min = 1, max = 20, message = "Number must be between 1 and 20 characters")
        String number,

        @Size(max = 100, message = "Complement cannot exceed 100 characters")
        String complement
) {

    public static ContactPoint toEntity(ContactPointInput input) {
        return new ContactPoint(
                input.name(),
                input.phone(),
                input.zipCode(),
                input.street(),
                input.number(),
                input.complement()
        );
    }
}