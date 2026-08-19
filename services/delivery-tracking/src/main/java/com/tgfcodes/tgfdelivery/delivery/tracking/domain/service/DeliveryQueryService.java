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
@Transactional(readOnly = true)
@Service
public class DeliveryQueryService {

    private final DeliveryRepository deliveryRepository;

    public Delivery findById(UUID deliveryId) {
        return deliveryRepository.findById(deliveryId).orElseThrow(() -> new DeliveryNotFoundException(deliveryId));
    }
}