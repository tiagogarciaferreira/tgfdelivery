package com.tgfcodes.tgfdelivery.courier.management.domain.service;

import com.tgfcodes.tgfdelivery.courier.management.api.input.CourierPayoutCalculationInput;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NullMarked;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;

@NullMarked
@RequiredArgsConstructor
@Service
public class CourierPayoutService {

    public BigDecimal calculate(CourierPayoutCalculationInput courierPayoutCalculationInput) {
        return BigDecimal.valueOf(10.0)
                .multiply(BigDecimal.valueOf(courierPayoutCalculationInput.distanceInKm()))
                .setScale(2, RoundingMode.HALF_EVEN);
    }
}