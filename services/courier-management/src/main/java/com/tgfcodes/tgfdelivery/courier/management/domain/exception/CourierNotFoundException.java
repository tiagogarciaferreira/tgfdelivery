package com.tgfcodes.tgfdelivery.courier.management.domain.exception;

import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.UUID;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@ResponseStatus(value = NOT_FOUND)
public class CourierNotFoundException extends DomainException {

    public CourierNotFoundException(UUID courierId) {
        super("Courier %s was not found".formatted(courierId));
    }
}