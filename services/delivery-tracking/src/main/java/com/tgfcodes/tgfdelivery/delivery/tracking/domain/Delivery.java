package com.tgfcodes.tgfdelivery.delivery.tracking.domain;

import lombok.*;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static java.util.Objects.isNull;

@NoArgsConstructor(access = AccessLevel.PACKAGE)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Setter(AccessLevel.PRIVATE)
@Getter
public class Delivery {

    @EqualsAndHashCode.Include
    private UUID id;

    private UUID courierId;

    private Instant placedAt;

    private Instant assignedAt;

    private Instant deliveredAt;

    private Instant expectedDeliveryAt;

    private Instant fulfilledAt;

    private BigDecimal distanceFee;

    private BigDecimal courierPayout;

    private BigDecimal totalCost;

    private Integer totalItems;

    private DeliveryStatus status;

    private ContactPoint sender;

    private ContactPoint recipient;

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
        Item item = Item.brandNew(name, quantity);
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
                .orElseThrow();/*TODO Create Exception */

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
        this.setStatus(DeliveryStatus.WAITING_FOR_COURIER);
        this.setPlacedAt(Instant.now());
    }

    public void pickup(UUID courierId) {
        this.setCourierId(courierId);
        this.setStatus(DeliveryStatus.IN_TRANSIT);
        this.setAssignedAt(Instant.now());
    }

    public void markAsDelivered() {
        this.setStatus(DeliveryStatus.DELIVERED);
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
            /*TODO Create Exception */
        }
        if (!getStatus().equals(DeliveryStatus.DRAFT)) {
            /*TODO Create Exception */
        }
    }

    private void verifyIfCanBeEdited() {
        if (!getStatus().equals(DeliveryStatus.DRAFT)) {
            /*TODO Create Exception */
        }
    }

    private boolean isFilled() {
        return isNull(this.getSender()) && isNull(this.getRecipient()) && isNull(this.getTotalCost());
    }
}