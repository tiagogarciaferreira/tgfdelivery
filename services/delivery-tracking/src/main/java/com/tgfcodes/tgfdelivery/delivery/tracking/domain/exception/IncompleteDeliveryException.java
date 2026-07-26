package com.tgfcodes.tgfdelivery.delivery.tracking.domain.exception;

import java.util.UUID;

public class IncompleteDeliveryException extends DomainException {

    public IncompleteDeliveryException(UUID deliveryId) {
        super("Delivery %s cannot be placed because it is incomplete. Mandatory preparation details are missing."
                .formatted(deliveryId));
    }
}
