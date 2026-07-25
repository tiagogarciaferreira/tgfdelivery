package com.tgfcodes.tgfdelivery.delivery.tracking.domain;

import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Delivery {

    @EqualsAndHashCode.Include
    private UUID uuid;

    private UUID courierId;

    private Instant placedAt;

    private Instant assignedAt;

    private Instant deliveredAt;

    private Instant expectedDeliveryAt;

    private Instant fulfilledAt;

    private BigDecimal distanceFee;

    private BigDecimal courierPayout;

    private BigDecimal totalCost;

    private Integer totalItems;

    private DeliveryStatus status;

    private ContactPoint sender;

    private ContactPoint recipient;

    private List<Item> items = new ArrayList<>();
}