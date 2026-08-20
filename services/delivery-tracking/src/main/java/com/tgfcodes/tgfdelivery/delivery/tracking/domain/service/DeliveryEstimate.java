package com.tgfcodes.tgfdelivery.delivery.tracking.domain.service;

import java.time.Duration;

public record DeliveryEstimate(

        Duration estimatedTime,

        Double distanceInKm
) {
}