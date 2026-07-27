package com.tgfcodes.tgfdelivery.delivery.tracking.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "tb_items")
@NoArgsConstructor(access = AccessLevel.PACKAGE)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Setter(AccessLevel.PRIVATE)
@Getter
public class Item {

    @Id
    @EqualsAndHashCode.Include
    private UUID id;

    private String name;

    @Setter(AccessLevel.PACKAGE)
    private Integer quantity;

    @ManyToOne(optional = false)
    @Getter(AccessLevel.PRIVATE)
    private Delivery delivery;

    public static Item brandNew(String name, Integer quantity, Delivery delivery) {
        Item item = new Item();
        item.setId(UUID.randomUUID());
        item.setName(name);
        item.setQuantity(quantity);
        item.setDelivery(delivery);
        return item;
    }
}