package com.tgfcodes.tgfdelivery.courier.management.domain.repository;

import com.tgfcodes.tgfdelivery.courier.management.domain.model.Courier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface CourierRepository extends JpaRepository<Courier, UUID> {

    Optional<Courier> findTop1ByOrderByLastFulFilledDeliveryAtAsc();

    Optional<Courier> findByPendingDeliveries_id(UUID deliveryId);
}
