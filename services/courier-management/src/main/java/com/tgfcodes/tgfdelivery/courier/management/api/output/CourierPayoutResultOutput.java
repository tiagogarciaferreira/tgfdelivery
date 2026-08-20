package com.tgfcodes.tgfdelivery.courier.management.api.output;

import java.math.BigDecimal;

public record CourierPayoutResultOutput(
        BigDecimal payoutFee
) {
}