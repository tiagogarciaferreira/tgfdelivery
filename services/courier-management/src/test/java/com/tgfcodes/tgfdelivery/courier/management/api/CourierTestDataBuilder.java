package com.tgfcodes.tgfdelivery.courier.management.api;

import com.tgfcodes.tgfdelivery.courier.management.api.input.CourierInput;
import com.tgfcodes.tgfdelivery.courier.management.api.input.CourierPayoutCalculationInput;
import com.tgfcodes.tgfdelivery.courier.management.domain.model.Courier;

import java.math.BigDecimal;

public class CourierTestDataBuilder {

    private CourierTestDataBuilder() {
        throw new IllegalStateException("Utility class");
    }

    public static Courier samwiseCourier() {
        return Courier.brandNew("Samwise Gamgee", "+5511999999999");
    }

    public static CourierInput validCourierInput() {
        return new CourierInput("Samwise Gamgee", "+5511999999999");
    }

    public static CourierInput invalidCourierInputBlankName() {
        return new CourierInput("", "+5511999999999");
    }

    public static CourierInput invalidCourierInputBadPhone() {
        return new CourierInput("Samwise Gamgee", "123");
    }

    public static CourierPayoutCalculationInput validPayoutInput() {
        return new CourierPayoutCalculationInput(15.5);
    }

    public static CourierPayoutCalculationInput invalidPayoutInput() {
        return new CourierPayoutCalculationInput(-5.0);
    }

    public static BigDecimal expectedPayoutFee() {
        return new BigDecimal("25.50");
    }
}