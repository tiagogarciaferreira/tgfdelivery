package com.tgfcodes.tgfdelivery.delivery.tracking.domain.repository;

import com.tgfcodes.tgfdelivery.delivery.tracking.domain.model.Delivery;
import com.tgfcodes.tgfdelivery.delivery.tracking.domain.model.DeliveryTestDataBuilder;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class DeliveryRepositoryTest {

    private final DeliveryRepository deliveryRepository;

    @Autowired
    DeliveryRepositoryTest(final DeliveryRepository deliveryRepository) {
        this.deliveryRepository = deliveryRepository;
    }

    @Test
    void shouldPersist() {
        Delivery delivery = Delivery.draft();
        delivery.editPreparationDetails(DeliveryTestDataBuilder.aPreparationDetails());
        delivery.addItem("PC", 5);
        delivery.addItem("TV", 2);

        deliveryRepository.saveAndFlush(delivery);
        Delivery persistedDelivery = deliveryRepository.findById(delivery.getId()).orElseThrow();
        assertThat(persistedDelivery.getItems()).hasSize(delivery.getItems().size());
    }
}