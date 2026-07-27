package com.tgfcodes.tgfdelivery.delivery.tracking.domain;


import jakarta.persistence.Embeddable;
import lombok.*;

@Embeddable
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PACKAGE)
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
