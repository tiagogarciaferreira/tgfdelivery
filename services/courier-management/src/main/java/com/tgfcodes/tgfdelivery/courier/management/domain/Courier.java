package com.tgfcodes.tgfdelivery.courier.management.domain;

import com.tgfcodes.tgfdelivery.courier.management.domain.exception.AssignedDeliveryNotFoundException;
import lombok.*;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Getter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@NoArgsConstructor(access = AccessLevel.PACKAGE)
@Setter(AccessLevel.PRIVATE)
public class Courier {

    @EqualsAndHashCode.Include
    private UUID id;

    @Setter(AccessLevel.PUBLIC)
    private String name;

    @Setter(AccessLevel.PUBLIC)
    private String phone;

    private Integer fulFilledDeliveryQuantity;

    private Integer pendingDeliveryQuantity;

    private Instant lastFulFilledDeliveryAt;

    private List<AssignedDelivery> pendingDeliveries = new ArrayList<>();

    public List<AssignedDelivery> getPendingDeliveries() {
        return Collections.unmodifiableList(pendingDeliveries);
    }

    public static Courier brandNew(String name, String phone) {
        Courier courier = new Courier();
        courier.setId(UUID.randomUUID());
        courier.setName(name);
        courier.setPhone(phone);
        courier.setPendingDeliveryQuantity(0);
        courier.setFulFilledDeliveryQuantity(0);
        return courier;
    }

    public void assign(UUID deliveryId) {
        this.pendingDeliveries.add(AssignedDelivery.pending(deliveryId));
        this.pendingDeliveryQuantity++;
    }

    public void fulFilled(UUID deliveryId) {
        boolean removed = this.pendingDeliveries.removeIf(delivery -> delivery.getId().equals(deliveryId));
        if (!removed) {
            throw new AssignedDeliveryNotFoundException(deliveryId);
        }

        this.pendingDeliveryQuantity--;
        this.fulFilledDeliveryQuantity++;
        this.lastFulFilledDeliveryAt = Instant.now();
    }
}