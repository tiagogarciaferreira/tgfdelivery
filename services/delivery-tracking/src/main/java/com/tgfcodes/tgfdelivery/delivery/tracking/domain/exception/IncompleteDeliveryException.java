package com.tgfcodes.tgfdelivery.delivery.tracking.domain.exception;

import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.UUID;

import static org.springframework.http.HttpStatus.CONFLICT;

@ResponseStatus(value = CONFLICT)
public class IncompleteDeliveryException extends DomainException {

    public IncompleteDeliveryException(UUID deliveryId) {
        super("Delivery %s cannot be placed because it is incomplete. Mandatory preparation details are missing."
                .formatted(deliveryId));
    }
}
