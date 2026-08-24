package com.tgfcodes.tgfdelivery.courier.management.domain.exception;

import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.UUID;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@ResponseStatus(value = NOT_FOUND)
public class AssignedDeliveryNotFoundException extends DomainException {

    public AssignedDeliveryNotFoundException(UUID deliveryId) {
        super("Delivery with ID %s was not found in the pending deliveries list".formatted(deliveryId));
    }
}