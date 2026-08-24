package com.tgfcodes.tgfdelivery.delivery.tracking.domain.exception;

import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.UUID;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@ResponseStatus(value = NOT_FOUND)
public class DeliveryItemNotFoundException extends DomainException {

    public DeliveryItemNotFoundException(UUID deliveryId, UUID itemId) {
        super("Item %s was not found in delivery %s".formatted(itemId, deliveryId));
    }
}