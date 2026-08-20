package com.tgfcodes.tgfdelivery.courier.management.domain.service;

import com.tgfcodes.tgfdelivery.courier.management.domain.exception.CourierNotFoundException;
import com.tgfcodes.tgfdelivery.courier.management.domain.model.Courier;
import com.tgfcodes.tgfdelivery.courier.management.domain.repository.CourierRepository;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NullMarked;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@NullMarked
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class CourierQueryService {

    private final CourierRepository courierRepository;

    public Courier findById(UUID courierId) {
        return courierRepository.findById(courierId).orElseThrow(() -> new CourierNotFoundException(courierId));
    }

    public Page<Courier> search(Pageable pageable) {
        return courierRepository.findAll(pageable);
    }
}