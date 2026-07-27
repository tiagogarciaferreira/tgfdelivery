package com.tgfcodes.tgfdelivery.courier.management.domain.exception;

import java.util.UUID;

public class AssignedDeliveryNotFoundException extends DomainException {

    public AssignedDeliveryNotFoundException(UUID deliveryId) {
        super("Delivery with ID %s was not found in the pending deliveries list".formatted(deliveryId));
    }
}