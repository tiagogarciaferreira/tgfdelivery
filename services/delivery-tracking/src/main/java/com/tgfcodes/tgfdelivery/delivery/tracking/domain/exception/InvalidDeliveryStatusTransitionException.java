package com.tgfcodes.tgfdelivery.delivery.tracking.domain.exception;

import com.tgfcodes.tgfdelivery.delivery.tracking.domain.model.DeliveryStatus;

import java.util.UUID;

public class InvalidDeliveryStatusTransitionException extends DomainException {

    public InvalidDeliveryStatusTransitionException(UUID deliveryId, DeliveryStatus currentStatus,
                                                    DeliveryStatus targetStatus) {
        super("Invalid status transition for delivery %s. Cannot transition from %s to %s."
                .formatted(deliveryId, currentStatus, targetStatus));
    }
}
