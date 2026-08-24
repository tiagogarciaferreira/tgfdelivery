package com.tgfcodes.tgfdelivery.delivery.tracking.domain.exception;

import com.tgfcodes.tgfdelivery.delivery.tracking.domain.model.DeliveryStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.UUID;

import static org.springframework.http.HttpStatus.CONFLICT;

@ResponseStatus(value = CONFLICT)
public class InvalidDeliveryStatusTransitionException extends DomainException {

    public InvalidDeliveryStatusTransitionException(UUID deliveryId, DeliveryStatus currentStatus,
                                                    DeliveryStatus targetStatus) {
        super("Invalid status transition for delivery %s. Cannot transition from %s to %s."
                .formatted(deliveryId, currentStatus, targetStatus));
    }
}
