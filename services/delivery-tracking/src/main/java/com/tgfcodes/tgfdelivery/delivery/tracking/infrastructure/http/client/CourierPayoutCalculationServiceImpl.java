package com.tgfcodes.tgfdelivery.delivery.tracking.infrastructure.http.client;

import com.tgfcodes.tgfdelivery.delivery.tracking.domain.service.CourierPayoutCalculationService;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NullMarked;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@NullMarked
@RequiredArgsConstructor
@Service
public class CourierPayoutCalculationServiceImpl implements CourierPayoutCalculationService {

    private final CourierAPIClient courierAPIClient;

    @Override
    public BigDecimal calculatePayout(Double distanceInKm) {
        CourierPayoutCalculationInput courierPayoutCalculationInput = new CourierPayoutCalculationInput(distanceInKm);
        CourierPayoutResultOutput payoutResultOutput = courierAPIClient.payoutCalculation(courierPayoutCalculationInput);
        return payoutResultOutput.payoutFee();
    }
}