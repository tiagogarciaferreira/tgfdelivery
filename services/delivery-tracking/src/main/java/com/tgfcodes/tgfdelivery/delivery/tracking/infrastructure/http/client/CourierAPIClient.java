package com.tgfcodes.tgfdelivery.delivery.tracking.infrastructure.http.client;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.PostExchange;

@HttpExchange("/api/v1/couriers")
public interface CourierAPIClient {

    @PostExchange(value = "/payout-calculation")
    CourierPayoutResultOutput payoutCalculation(@Valid @RequestBody CourierPayoutCalculationInput courierPayoutCalculationInput);
}