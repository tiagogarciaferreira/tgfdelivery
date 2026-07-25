package com.tgfcodes.tgfdelivery.delivery.tracking.domain;


import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@AllArgsConstructor
@EqualsAndHashCode
@Getter
public class ContactPoint {

    private String name;

    private String phone;

    private String zipCode;

    private String street;

    private String number;

    private String complement;
}
