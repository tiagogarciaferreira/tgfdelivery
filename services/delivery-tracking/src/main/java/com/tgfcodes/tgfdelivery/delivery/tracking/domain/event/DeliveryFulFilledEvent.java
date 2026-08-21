package com.tgfcodes.tgfdelivery.delivery.tracking.domain.event;

import java.time.Instant;
import java.util.UUID;

public record DeliveryFulFilledEvent(
        UUID deliveryId,
        Instant occurredAt
) {
}