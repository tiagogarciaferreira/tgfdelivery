package com.tgfcodes.tgfdelivery.delivery.tracking.domain;

import lombok.EqualsAndHashCode;

import java.util.UUID;

@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Item {

    @EqualsAndHashCode.Include
    private UUID id;

    private String name;

    private Integer quantity;
}