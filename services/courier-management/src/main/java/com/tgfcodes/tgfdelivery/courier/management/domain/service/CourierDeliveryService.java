package com.tgfcodes.tgfdelivery.courier.management.domain.service;

import com.tgfcodes.tgfdelivery.courier.management.domain.exception.CourierNotAvailableException;
import com.tgfcodes.tgfdelivery.courier.management.domain.model.Courier;
import com.tgfcodes.tgfdelivery.courier.management.domain.repository.CourierRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NullMarked;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Slf4j
@NullMarked
@RequiredArgsConstructor
@Service
@Transactional
public class CourierDeliveryService {

    private final CourierRepository courierRepository;

    public void assign(UUID deliveryId) {
        Courier courier = courierRepository.findTop1ByOrderByLastFulFilledDeliveryAtAsc()
                .orElseThrow(() -> new CourierNotAvailableException(deliveryId));

        courier.assign(deliveryId);
        courierRepository.save(courier);
        log.info("Assigned delivery {} to courier {}", deliveryId, courier.getId());
    }

    public void fulfill(UUID deliveryId) {
        Courier courier = courierRepository.findByPendingDeliveries_id(deliveryId)
                .orElseThrow(() -> new CourierNotAvailableException(deliveryId));

        courier.fulFilled(deliveryId);
        courierRepository.save(courier);
        log.info("Fulfilled delivery {} by courier {}", deliveryId, courier.getId());
    }
}