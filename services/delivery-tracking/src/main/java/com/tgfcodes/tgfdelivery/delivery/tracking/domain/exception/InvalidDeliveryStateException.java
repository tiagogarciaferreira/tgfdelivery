package com.tgfcodes.tgfdelivery.delivery.tracking.domain.exception;

import com.tgfcodes.tgfdelivery.delivery.tracking.domain.model.DeliveryStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.UUID;

import static org.springframework.http.HttpStatus.CONFLICT;

@ResponseStatus(value = CONFLICT)
public class InvalidDeliveryStateException extends DomainException {

    public InvalidDeliveryStateException(UUID deliveryId, DeliveryStatus currentStatus) {
        super("Operation not allowed for delivery %s. Current state is %s, expected DRAFT."
                .formatted(deliveryId, currentStatus));
    }
}
