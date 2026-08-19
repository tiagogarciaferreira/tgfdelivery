package com.tgfcodes.tgfdelivery.delivery.tracking.domain.exception;

import java.util.UUID;

public class DeliveryNotFoundException extends DomainException {

    public DeliveryNotFoundException(UUID deliveryId) {
        super("Delivery %s was not found".formatted(deliveryId));
    }
}