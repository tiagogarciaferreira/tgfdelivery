package com.tgfcodes.tgfdelivery.delivery.tracking.domain;


import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;

@AllArgsConstructor
@EqualsAndHashCode
public class ContactPoint {

    private String name;

    private String phone;

    private String zipCode;

    private String street;

    private String number;

    private String complement;
}
