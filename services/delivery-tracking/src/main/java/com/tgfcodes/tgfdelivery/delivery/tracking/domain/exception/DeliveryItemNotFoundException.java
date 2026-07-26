package com.tgfcodes.tgfdelivery.delivery.tracking.domain.exception;

import java.util.UUID;

public class DeliveryItemNotFoundException extends DomainException {

    public DeliveryItemNotFoundException(UUID deliveryId, UUID itemId) {
        super("Item %s was not found in delivery %s".formatted(itemId, deliveryId));
    }
}