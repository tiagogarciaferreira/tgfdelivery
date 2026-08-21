package com.tgfcodes.tgfdelivery.courier.management.infrastructure.event;

import java.time.Instant;
import java.util.UUID;

public record DeliveryPlacedIntegrationEvent(
        UUID deliveryId,
        Instant occurredAt
) {
}