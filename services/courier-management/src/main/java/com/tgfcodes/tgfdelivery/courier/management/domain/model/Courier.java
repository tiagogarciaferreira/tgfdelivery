package com.tgfcodes.tgfdelivery.courier.management.domain.model;

import com.tgfcodes.tgfdelivery.courier.management.domain.exception.AssignedDeliveryNotFoundException;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "tb_couriers")
@Getter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@NoArgsConstructor(access = AccessLevel.PACKAGE)
@Setter(AccessLevel.PRIVATE)
public class Courier {

    @Id
    @EqualsAndHashCode.Include
    private UUID id;

    @Setter(AccessLevel.PUBLIC)
    private String name;

    @Setter(AccessLevel.PUBLIC)
    private String phone;

    @Column(name = "ful_filled_delivery_quantity")
    private Integer fulFilledDeliveryQuantity;

    @Column(name = "pending_delivery_quantity")
    private Integer pendingDeliveryQuantity;

    @Column(name = "last_ful_filled_delivery_at")
    private Instant lastFulFilledDeliveryAt;

    @OneToMany(mappedBy = "courier", orphanRemoval = true, cascade = CascadeType.ALL)
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
        this.pendingDeliveries.add(AssignedDelivery.pending(deliveryId, this));
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