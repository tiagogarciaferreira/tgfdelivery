package com.tgfcodes.tgfdelivery.delivery.tracking.infrastructure.fake;

import com.tgfcodes.tgfdelivery.delivery.tracking.domain.model.ContactPoint;
import com.tgfcodes.tgfdelivery.delivery.tracking.domain.service.DeliveryEstimate;
import com.tgfcodes.tgfdelivery.delivery.tracking.domain.service.DeliveryTimeEstimationService;
import org.jspecify.annotations.NullMarked;
import org.springframework.stereotype.Service;

import java.time.Duration;

@NullMarked
@Service
public class DeliveryTimeEstimationServiceFakeImpl implements DeliveryTimeEstimationService {

    @Override
    public DeliveryEstimate estimate(ContactPoint sender, ContactPoint receiver) {
        return new DeliveryEstimate(Duration.ofMinutes(30), 3.5);
    }
}