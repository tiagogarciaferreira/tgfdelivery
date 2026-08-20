package com.tgfcodes.tgfdelivery.delivery.tracking.api.output;

import com.tgfcodes.tgfdelivery.delivery.tracking.domain.model.Delivery;
import com.tgfcodes.tgfdelivery.delivery.tracking.domain.model.Item;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import static java.util.Objects.isNull;

public record DeliveryOutput(
        UUID id,

        UUID courierId,

        String status,

        Instant placedAt,

        Instant assignedAt,

        Instant deliveredAt,

        Instant expectedDeliveryAt,

        Instant fulfilledAt,

        BigDecimal distanceFee,

        BigDecimal courierPayout,

        BigDecimal totalCost,

        Integer totalItems,

        ContactPointOutput sender,

        ContactPointOutput recipient,

        List<ItemOutput> items
) {
    public static DeliveryOutput toResponse(Delivery delivery) {
        Objects.requireNonNull(delivery, "Delivery cannot be null");

        List<Item> validItems = isNull(delivery.getItems()) ? List.of() : delivery.getItems();
        return new DeliveryOutput(
                delivery.getId(),
                delivery.getCourierId(),
                delivery.getStatus().name(),
                delivery.getPlacedAt(),
                delivery.getAssignedAt(),
                delivery.getDeliveredAt(),
                delivery.getExpectedDeliveryAt(),
                delivery.getFulfilledAt(),
                delivery.getDistanceFee(),
                delivery.getCourierPayout(),
                delivery.getTotalCost(),
                delivery.getTotalItems(),
                ContactPointOutput.toResponse(delivery.getSender()),
                ContactPointOutput.toResponse(delivery.getRecipient()),
                validItems.stream().map(ItemOutput::toResponse).toList()
        );
    }
}