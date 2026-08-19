package com.tgfcodes.tgfdelivery.delivery.tracking.api.output;

import com.tgfcodes.tgfdelivery.delivery.tracking.domain.model.ContactPoint;

import java.util.Objects;

public record ContactPointResponse(
        String name,

        String phone,

        String zipCode,

        String street,

        String number,

        String complement
) {
    public static ContactPointResponse toResponse(ContactPoint contactPoint) {
        Objects.requireNonNull(contactPoint, "ContactPoint cannot be null");
        return new ContactPointResponse(
                contactPoint.getName(),
                contactPoint.getPhone(),
                contactPoint.getZipCode(),
                contactPoint.getStreet(),
                contactPoint.getNumber(),
                contactPoint.getComplement()
        );
    }
}