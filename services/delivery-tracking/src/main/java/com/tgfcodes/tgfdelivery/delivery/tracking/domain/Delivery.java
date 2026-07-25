package com.tgfcodes.tgfdelivery.delivery.tracking.domain;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@NoArgsConstructor(access = AccessLevel.PACKAGE)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Delivery {

    @EqualsAndHashCode.Include
    private UUID id;

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

    public static Delivery draft() {
        Delivery delivery = new Delivery();
        delivery.id = UUID.randomUUID();
        delivery.status = DeliveryStatus.DRAFT;
        delivery.totalItems = 0;
        delivery.totalCost = BigDecimal.ZERO;
        delivery.distanceFee = BigDecimal.ZERO;
        delivery.courierPayout = BigDecimal.ZERO;
        return delivery;
    }
}