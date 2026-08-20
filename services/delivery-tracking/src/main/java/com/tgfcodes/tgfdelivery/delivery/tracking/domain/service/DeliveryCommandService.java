package com.tgfcodes.tgfdelivery.delivery.tracking.domain.service;

import com.tgfcodes.tgfdelivery.delivery.tracking.api.input.ContactPointInput;
import com.tgfcodes.tgfdelivery.delivery.tracking.api.input.DeliveryInput;
import com.tgfcodes.tgfdelivery.delivery.tracking.domain.exception.DeliveryNotFoundException;
import com.tgfcodes.tgfdelivery.delivery.tracking.domain.model.ContactPoint;
import com.tgfcodes.tgfdelivery.delivery.tracking.domain.model.Delivery;
import com.tgfcodes.tgfdelivery.delivery.tracking.domain.model.PreparationDetails;
import com.tgfcodes.tgfdelivery.delivery.tracking.domain.repository.DeliveryRepository;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NullMarked;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.UUID;

@NullMarked
@RequiredArgsConstructor
@Transactional
@Service
public class DeliveryCommandService {

    private final DeliveryRepository deliveryRepository;

    private final DeliveryTimeEstimationService deliveryTimeEstimationService;

    private final CourierPayoutCalculationService courierPayoutCalculationService;

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
        ContactPoint sender = ContactPointInput.toEntity(deliveryInput.sender());
        ContactPoint recipient = ContactPointInput.toEntity(deliveryInput.recipient());

        DeliveryEstimate estimate = deliveryTimeEstimationService.estimate(sender, recipient);
        BigDecimal calculatedPayout = courierPayoutCalculationService.calculatePayout(estimate.distanceInKm());
        BigDecimal distanceFee = calculateFee(estimate.distanceInKm());

        return new PreparationDetails(
                sender,
                recipient,
                distanceFee,
                calculatedPayout,
                estimate.estimatedTime()
        );
    }

    private BigDecimal calculateFee(Double distanceInKm) {
        return BigDecimal.valueOf(3.5)
                .multiply(BigDecimal.valueOf(distanceInKm))
                .setScale(2, RoundingMode.HALF_EVEN);
    }
}