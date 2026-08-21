package com.tgfcodes.tgfdelivery.courier.management.domain.exception;

import java.util.UUID;

public class CourierNotAvailableException extends DomainException {

    public CourierNotAvailableException(UUID deliveryId) {
        super("Courier is not available for delivery %s.".formatted(deliveryId));
    }
}