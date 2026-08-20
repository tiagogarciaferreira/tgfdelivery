package com.tgfcodes.tgfdelivery.delivery.tracking.infrastructure.http.client;

import java.math.BigDecimal;

public record CourierPayoutResultOutput(
        BigDecimal payoutFee
) {
}