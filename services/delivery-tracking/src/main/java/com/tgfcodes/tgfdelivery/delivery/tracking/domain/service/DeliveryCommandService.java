package com.tgfcodes.tgfdelivery.delivery.tracking.domain.service;

import com.tgfcodes.tgfdelivery.delivery.tracking.api.input.ContactPointInput;
import com.tgfcodes.tgfdelivery.delivery.tracking.api.input.DeliveryInput;
import com.tgfcodes.tgfdelivery.delivery.tracking.domain.exception.DeliveryNotFoundException;
import com.tgfcodes.tgfdelivery.delivery.tracking.domain.model.Delivery;
import com.tgfcodes.tgfdelivery.delivery.tracking.domain.model.PreparationDetails;
import com.tgfcodes.tgfdelivery.delivery.tracking.domain.repository.DeliveryRepository;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NullMarked;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.UUID;

@NullMarked
@RequiredArgsConstructor
@Transactional
@Service
public class DeliveryCommandService {

    private final DeliveryRepository deliveryRepository;

    public Delivery draft(DeliveryInput deliveryInput) {
        Delivery delivery = Delivery.draft();
        deliveryInput.items().forEach(item -> delivery.addItem(item.name(), item.quantity()));

        PreparationDetails preparationDetails = buildPreparationDetails(deliveryInput);

        delivery.editPreparationDetails(preparationDetails);
        return deliveryRepository.save(delivery);
    }

    public Delivery edit(UUID deliveryId, DeliveryInput deliveryInput) {
        Delivery delivery = deliveryRepository.findById(deliveryId)
                .orElseThrow(() -> new DeliveryNotFoundException(deliveryId));

        delivery.clearItems();
        deliveryInput.items().forEach(item -> delivery.addItem(item.name(), item.quantity()));

        PreparationDetails preparationDetails = buildPreparationDetails(deliveryInput);
        delivery.editPreparationDetails(preparationDetails);

        return deliveryRepository.save(delivery);
    }

    private PreparationDetails buildPreparationDetails(DeliveryInput deliveryInput) {
        return new PreparationDetails(
                ContactPointInput.toEntity(deliveryInput.sender()),
                ContactPointInput.toEntity(deliveryInput.recipient()),
                new BigDecimal("10"),
                new BigDecimal("10"),
                Duration.ofHours(3)
        );
    }
}
