package com.tgfcodes.tgfdelivery.delivery.tracking.domain.repository;

import com.tgfcodes.tgfdelivery.delivery.tracking.domain.model.Delivery;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface DeliveryRepository extends JpaRepository<Delivery, UUID> {
}