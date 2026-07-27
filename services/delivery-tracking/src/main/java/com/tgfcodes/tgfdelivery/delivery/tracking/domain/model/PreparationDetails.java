package com.tgfcodes.tgfdelivery.delivery.tracking.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.Duration;

@Getter
@AllArgsConstructor
@Builder
public class PreparationDetails {

    private ContactPoint sender;

    private ContactPoint recipient;

    private BigDecimal distanceFee;

    private BigDecimal courierPayout;

    private Duration expectedDeliveryTime;
}