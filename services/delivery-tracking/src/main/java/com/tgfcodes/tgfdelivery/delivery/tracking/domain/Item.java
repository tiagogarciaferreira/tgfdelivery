package com.tgfcodes.tgfdelivery.delivery.tracking.domain;

import lombok.*;

import java.util.UUID;

@NoArgsConstructor(access = AccessLevel.PACKAGE)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Setter(AccessLevel.PRIVATE)
@Getter
public class Item {

    @EqualsAndHashCode.Include
    private UUID id;

    private String name;

    @Setter(AccessLevel.PACKAGE)
    private Integer quantity;

    public static Item brandNew(String name, Integer quantity) {
        Item item = new Item();
        item.id = UUID.randomUUID();
        item.name = name;
        item.quantity = quantity;
        return item;
    }
}