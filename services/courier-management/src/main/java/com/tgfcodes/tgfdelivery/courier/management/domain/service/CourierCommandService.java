package com.tgfcodes.tgfdelivery.courier.management.domain.service;

import com.tgfcodes.tgfdelivery.courier.management.api.input.CourierInput;
import com.tgfcodes.tgfdelivery.courier.management.domain.exception.CourierNotFoundException;
import com.tgfcodes.tgfdelivery.courier.management.domain.model.Courier;
import com.tgfcodes.tgfdelivery.courier.management.domain.repository.CourierRepository;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NullMarked;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@NullMarked
@RequiredArgsConstructor
@Transactional
@Service
public class CourierCommandService {

    private final CourierRepository courierRepository;

    public Courier create(CourierInput courierInput) {
        Courier courier = Courier.brandNew(courierInput.name(), courierInput.phone());
        return courierRepository.save(courier);
    }

    public Courier update(UUID courierId, CourierInput courierInput) {
        Courier courier = courierRepository.findById(courierId).orElseThrow(() -> new CourierNotFoundException(courierId));
        courier.setName(courierInput.name());
        courier.setPhone(courierInput.phone());
        return courierRepository.save(courier);
    }
}