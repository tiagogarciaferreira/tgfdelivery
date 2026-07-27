package com.tgfcodes.tgfdelivery.delivery.tracking.domain.model;

import com.tgfcodes.tgfdelivery.delivery.tracking.domain.ContactPoint;
import com.tgfcodes.tgfdelivery.delivery.tracking.domain.Delivery;
import com.tgfcodes.tgfdelivery.delivery.tracking.domain.PreparationDetails;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.UUID;

public class DeliveryTestDataBuilder {

    public static final UUID COURIER_ID = UUID.fromString("07579c70-8cba-417f-9968-43a5c12de215");

    private DeliveryTestDataBuilder() {
        throw new IllegalStateException("Utility class");
    }

    public static Delivery draftDelivery() {
        return Delivery.draft();
    }

    public static Delivery filledDraftDelivery() {
        var delivery = Delivery.draft();
        var details = aPreparationDetails();
        delivery.editPreparationDetails(details);
        return delivery;
    }

    public static Delivery deliveryWaitingForCourier() {
        var delivery = filledDraftDelivery();
        delivery.place();
        return delivery;
    }

    public static Delivery deliveryInTransit() {
        var delivery = deliveryWaitingForCourier();
        delivery.pickup(COURIER_ID);
        return delivery;
    }

    public static ContactPoint aSender() {
        return new ContactPoint(
                "Central Grill Restaurant",
                "+15550192834",
                "90210",
                "Sunset Boulevard",
                "100",
                "Suite 1"
        );
    }

    public static ContactPoint aRecipient() {
        return new ContactPoint(
                "John Doe",
                "+15550183746",
                "90211",
                "Ocean Avenue",
                "500",
                "Apt 4B"
        );
    }

    public static PreparationDetails aPreparationDetails() {
        return PreparationDetails.builder()
                .sender(aSender())
                .recipient(aRecipient())
                .distanceFee(new BigDecimal("12.50"))
                .courierPayout(new BigDecimal("8.00"))
                .expectedDeliveryTime(Duration.ofMinutes(45))
                .build();
    }
}