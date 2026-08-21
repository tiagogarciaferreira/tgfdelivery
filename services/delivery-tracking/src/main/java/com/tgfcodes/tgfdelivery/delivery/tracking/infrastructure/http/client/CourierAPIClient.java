package com.tgfcodes.tgfdelivery.delivery.tracking.infrastructure.http.client;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.PostExchange;

@HttpExchange("/api/v1/couriers")
public interface CourierAPIClient {

    @PostExchange(value = "/payout-calculation")
    @Retry(name = "Retry_CourierAPIClient_payoutCalculation")
    @CircuitBreaker(name = "CircuitBreaker_CourierAPIClient_payoutCalculation")
    CourierPayoutResultOutput payoutCalculation(@Valid @RequestBody CourierPayoutCalculationInput courierPayoutCalculationInput);
}