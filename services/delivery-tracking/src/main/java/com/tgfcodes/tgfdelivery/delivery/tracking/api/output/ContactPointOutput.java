package com.tgfcodes.tgfdelivery.delivery.tracking.api.output;

import com.tgfcodes.tgfdelivery.delivery.tracking.domain.model.ContactPoint;

import java.util.Objects;

public record ContactPointOutput(
        String name,

        String phone,

        String zipCode,

        String street,

        String number,

        String complement
) {
    public static ContactPointOutput toOutput(ContactPoint contactPoint) {
        Objects.requireNonNull(contactPoint, "ContactPoint cannot be null");
        return new ContactPointOutput(
                contactPoint.getName(),
                contactPoint.getPhone(),
                contactPoint.getZipCode(),
                contactPoint.getStreet(),
                contactPoint.getNumber(),
                contactPoint.getComplement()
        );
    }
}