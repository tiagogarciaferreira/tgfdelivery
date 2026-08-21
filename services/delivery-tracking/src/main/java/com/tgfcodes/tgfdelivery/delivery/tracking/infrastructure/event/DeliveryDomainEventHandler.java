package com.tgfcodes.tgfdelivery.delivery.tracking.infrastructure.event;

import com.tgfcodes.tgfdelivery.delivery.tracking.domain.event.DeliveryFulfilledEvent;
import com.tgfcodes.tgfdelivery.delivery.tracking.domain.event.DeliveryPickUpEvent;
import com.tgfcodes.tgfdelivery.delivery.tracking.domain.event.DeliveryPlacedEvent;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NullMarked;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import static com.tgfcodes.tgfdelivery.delivery.tracking.infrastructure.kafka.KafkaTopicConfig.DELIVERIES_V_1_EVENTS;

@NullMarked
@RequiredArgsConstructor
@Component
public class DeliveryDomainEventHandler {

    private final IntegrationEventPublisher integrationEventPublisher;

    @EventListener
    public void handle(DeliveryPlacedEvent event) {
        integrationEventPublisher.publish(event, event.deliveryId().toString(), DELIVERIES_V_1_EVENTS);
    }

    @EventListener
    public void handle(DeliveryPickUpEvent event) {
        integrationEventPublisher.publish(event, event.deliveryId().toString(), DELIVERIES_V_1_EVENTS);
    }

    @EventListener
    public void handle(DeliveryFulfilledEvent event) {
        integrationEventPublisher.publish(event, event.deliveryId().toString(), DELIVERIES_V_1_EVENTS);
    }
}