package com.tgfcodes.tgfdelivery.delivery.tracking.domain.exception;

import com.tgfcodes.tgfdelivery.delivery.tracking.domain.model.DeliveryStatus;

import java.util.UUID;

public class InvalidDeliveryStateException extends DomainException {

    public InvalidDeliveryStateException(UUID deliveryId, DeliveryStatus currentStatus) {
        super("Operation not allowed for delivery %s. Current state is %s, expected DRAFT."
                .formatted(deliveryId, currentStatus));
    }
}
