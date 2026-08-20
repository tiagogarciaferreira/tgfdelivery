package com.tgfcodes.tgfdelivery.courier.management.domain.exception;

import java.util.UUID;

public class CourierNotFoundException extends DomainException {

    public CourierNotFoundException(UUID courierId) {
        super("Courier %s was not found".formatted(courierId));
    }
}