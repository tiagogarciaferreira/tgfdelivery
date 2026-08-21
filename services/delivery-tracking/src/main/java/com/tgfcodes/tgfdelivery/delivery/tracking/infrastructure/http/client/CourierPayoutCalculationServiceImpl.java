package com.tgfcodes.tgfdelivery.delivery.tracking.infrastructure.http.client;

import com.tgfcodes.tgfdelivery.delivery.tracking.domain.service.CourierPayoutCalculationService;
import io.github.resilience4j.circuitbreaker.CallNotPermittedException;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NullMarked;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.ResourceAccessException;

import java.math.BigDecimal;

@NullMarked
@RequiredArgsConstructor
@Service
public class CourierPayoutCalculationServiceImpl implements CourierPayoutCalculationService {

    private final CourierAPIClient courierAPIClient;

    @Override
    public BigDecimal calculatePayout(Double distanceInKm) {
        try {
            CourierPayoutCalculationInput courierPayoutCalculationInput = new CourierPayoutCalculationInput(distanceInKm);
            CourierPayoutResultOutput payoutResultOutput = courierAPIClient.payoutCalculation(courierPayoutCalculationInput);
            return payoutResultOutput.payoutFee();

        } catch (ResourceAccessException ex) {
            throw new GatewayTimeoutException(ex);
        } catch (HttpServerErrorException | CallNotPermittedException | IllegalArgumentException ex) {
            throw new BadGatewayException(ex);
        }
    }
}