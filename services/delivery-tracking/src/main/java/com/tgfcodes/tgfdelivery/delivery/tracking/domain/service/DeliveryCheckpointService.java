package com.tgfcodes.tgfdelivery.delivery.tracking.domain.service;

import com.tgfcodes.tgfdelivery.delivery.tracking.domain.exception.DeliveryNotFoundException;
import com.tgfcodes.tgfdelivery.delivery.tracking.domain.model.Delivery;
import com.tgfcodes.tgfdelivery.delivery.tracking.domain.repository.DeliveryRepository;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NullMarked;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@NullMarked
@RequiredArgsConstructor
@Transactional
@Service
public class DeliveryCheckpointService {

    private final DeliveryRepository deliveryRepository;

    public void place(UUID deliveryId) {
        Delivery delivery = findDeliveryOrThrow(deliveryId);
        delivery.place();
        deliveryRepository.save(delivery);
    }

    public void pickup(UUID deliveryId, UUID courierId) {
        Delivery delivery = findDeliveryOrThrow(deliveryId);
        delivery.pickup(courierId);
        deliveryRepository.save(delivery);
    }

    public void complete(UUID deliveryId) {
        Delivery delivery = findDeliveryOrThrow(deliveryId);
        delivery.markAsDelivered();
        deliveryRepository.save(delivery);
    }

    private Delivery findDeliveryOrThrow(UUID deliveryId) {
        return deliveryRepository.findById(deliveryId).orElseThrow(() -> new DeliveryNotFoundException(deliveryId));
    }
}