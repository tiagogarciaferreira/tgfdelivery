package com.tgfcodes.tgfdelivery.courier.management.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "tb_assigned_deliveries")
@Getter
@NoArgsConstructor(access = AccessLevel.PACKAGE)
@Setter(AccessLevel.PRIVATE)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class AssignedDelivery {

    @Id
    @EqualsAndHashCode.Include
    private UUID id;

    @Column(name = "assigned_at")
    private Instant assignedAt;

    @ManyToOne(optional = false)
    @Getter(AccessLevel.PRIVATE)
    private Courier courier;

    public static AssignedDelivery pending(UUID deliveryId, Courier courier) {
        AssignedDelivery assignedDelivery = new AssignedDelivery();
        assignedDelivery.setId(deliveryId);
        assignedDelivery.setCourier(courier);
        assignedDelivery.setAssignedAt(Instant.now());
        return assignedDelivery;
    }
}