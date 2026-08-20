package com.tgfcodes.tgfdelivery.delivery.tracking.domain.service;

import com.tgfcodes.tgfdelivery.delivery.tracking.domain.model.ContactPoint;

public interface DeliveryTimeEstimationService {

    DeliveryEstimate estimate(ContactPoint sender, ContactPoint receiver);
}