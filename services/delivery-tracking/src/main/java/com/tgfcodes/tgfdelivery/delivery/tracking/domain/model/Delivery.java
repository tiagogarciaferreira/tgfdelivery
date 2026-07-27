package com.tgfcodes.tgfdelivery.delivery.tracking.domain.model;

import com.tgfcodes.tgfdelivery.delivery.tracking.domain.exception.DeliveryItemNotFoundException;
import com.tgfcodes.tgfdelivery.delivery.tracking.domain.exception.IncompleteDeliveryException;
import com.tgfcodes.tgfdelivery.delivery.tracking.domain.exception.InvalidDeliveryStateException;
import com.tgfcodes.tgfdelivery.delivery.tracking.domain.exception.InvalidDeliveryStatusTransitionException;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static java.util.Objects.isNull;

@Entity
@Table(name = "tb_deliveries")
@NoArgsConstructor(access = AccessLevel.PACKAGE)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Setter(AccessLevel.PRIVATE)
@Getter
public class Delivery {

    @Id
    @EqualsAndHashCode.Include
    private UUID id;

    @Column(name = "courier_id")
    private UUID courierId;

    @Column(name = "placed_at")
    private Instant placedAt;

    @Column(name = "assigned_at")
    private Instant assignedAt;

    @Column(name = "delivered_at")
    private Instant deliveredAt;

    @Column(name = "expected_delivery_at")
    private Instant expectedDeliveryAt;

    @Column(name = "fulfilled_at")
    private Instant fulfilledAt;

    @Column(name = "distance_fee")
    private BigDecimal distanceFee;

    @Column(name = "courier_payout")
    private BigDecimal courierPayout;

    @Column(name = "total_cost")
    private BigDecimal totalCost;

    @Column(name = "total_items")
    private Integer totalItems;

    @Enumerated(EnumType.STRING)
    private DeliveryStatus status;

    @Embedded
    @AttributeOverride(name = "name", column = @Column(name = "sender_name"))
    @AttributeOverride(name = "phone", column = @Column(name = "sender_phone"))
    @AttributeOverride(name = "zipCode", column = @Column(name = "sender_zip_code"))
    @AttributeOverride(name = "street", column = @Column(name = "sender_street"))
    @AttributeOverride(name = "number", column = @Column(name = "sender_number"))
    @AttributeOverride(name = "complement", column = @Column(name = "sender_complement"))
    private ContactPoint sender;

    @Embedded
    @AttributeOverride(name = "name", column = @Column(name = "recipient_name"))
    @AttributeOverride(name = "phone", column = @Column(name = "recipient_phone"))
    @AttributeOverride(name = "zipCode", column = @Column(name = "recipient_zip_code"))
    @AttributeOverride(name = "street", column = @Column(name = "recipient_street"))
    @AttributeOverride(name = "number", column = @Column(name = "recipient_number"))
    @AttributeOverride(name = "complement", column = @Column(name = "recipient_complement"))
    private ContactPoint recipient;

    @OneToMany(mappedBy = "delivery", orphanRemoval = true, cascade = CascadeType.ALL)
    private List<Item> items = new ArrayList<>();

    public static Delivery draft() {
        Delivery delivery = new Delivery();
        delivery.setId(UUID.randomUUID());
        delivery.setStatus(DeliveryStatus.DRAFT);
        delivery.setTotalItems(0);
        delivery.setTotalCost(BigDecimal.ZERO);
        delivery.setDistanceFee(BigDecimal.ZERO);
        delivery.setCourierPayout(BigDecimal.ZERO);
        return delivery;
    }

    public UUID addItem(String name, int quantity) {
        Item item = Item.brandNew(name, quantity, this);
        this.items.add(item);
        this.calculateTotalItems();
        return item.getId();
    }

    public void removeItem(UUID itemId) {
        this.items.removeIf(item -> item.getId().equals(itemId));
        this.calculateTotalItems();
    }

    public void changeItemQuantity(UUID itemId, int quantity) {
        Item foundItem = this.getItems().stream()
                .filter(item -> item.getId().equals(itemId))
                .findFirst()
                .orElseThrow(() -> new DeliveryItemNotFoundException(this.getId(), itemId));

        foundItem.setQuantity(quantity);
        this.calculateTotalItems();
    }

    public void editPreparationDetails(PreparationDetails details) {
        verifyIfCanBeEdited();
        this.setSender(details.getSender());
        this.setRecipient(details.getRecipient());
        this.setDistanceFee(details.getDistanceFee());
        this.setCourierPayout(details.getCourierPayout());
        this.setExpectedDeliveryAt(Instant.now().plus(details.getExpectedDeliveryTime()));
        this.setTotalCost(this.getDistanceFee().add(this.getCourierPayout()));
    }

    public void place() {
        verifyIfCanBePlaced();
        this.changeStatusTo(DeliveryStatus.WAITING_FOR_COURIER);
        this.setPlacedAt(Instant.now());
    }

    public void pickup(UUID courierId) {
        this.setCourierId(courierId);
        this.changeStatusTo(DeliveryStatus.IN_TRANSIT);
        this.setAssignedAt(Instant.now());
    }

    public void markAsDelivered() {
        this.changeStatusTo(DeliveryStatus.DELIVERED);
        this.setFulfilledAt(Instant.now());
        this.setDeliveredAt(Instant.now());
    }

    public void clearItems() {
        this.items.clear();
        this.calculateTotalItems();
    }

    public List<Item> getItems() {
        return Collections.unmodifiableList(this.items);
    }

    private void calculateTotalItems() {
        this.totalItems = this.items.stream().mapToInt(Item::getQuantity).sum();
    }

    private void verifyIfCanBePlaced() {
        if (!isFilled()) {
            throw new IncompleteDeliveryException(this.getId());
        }
        if (!getStatus().equals(DeliveryStatus.DRAFT)) {
            throw new InvalidDeliveryStateException(this.getId(), this.getStatus());
        }
    }

    private void verifyIfCanBeEdited() {
        if (!getStatus().equals(DeliveryStatus.DRAFT)) {
            throw new InvalidDeliveryStateException(this.getId(), this.getStatus());
        }
    }

    private boolean isFilled() {
        return !isNull(this.getSender()) && !isNull(this.getRecipient()) && !isNull(this.getTotalCost());
    }

    private void changeStatusTo(DeliveryStatus newStatus) {
        if (!isNull(newStatus) && this.getStatus().canNotChangeTo(newStatus)) {
            throw new InvalidDeliveryStatusTransitionException(this.getId(), this.getStatus(), newStatus);
        }
        this.setStatus(newStatus);
    }
}