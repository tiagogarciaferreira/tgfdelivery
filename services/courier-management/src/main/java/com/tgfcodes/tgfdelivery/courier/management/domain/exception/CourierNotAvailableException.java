package com.tgfcodes.tgfdelivery.courier.management.domain.exception;

import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.UUID;

import static org.springframework.http.HttpStatus.CONFLICT;

@ResponseStatus(value = CONFLICT)
public class CourierNotAvailableException extends DomainException {

    public CourierNotAvailableException(UUID deliveryId) {
        super("Courier is not available for delivery %s.".formatted(deliveryId));
    }
}