package com.tgfcodes.tgfdelivery.delivery.tracking.domain.exception;

import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.UUID;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@ResponseStatus(value = NOT_FOUND)
public class DeliveryNotFoundException extends DomainException {

    public DeliveryNotFoundException(UUID deliveryId) {
        super("Delivery %s was not found".formatted(deliveryId));
    }
}