package com.tgfcodes.tgfdelivery.courier.management.infrastructure.kafka;

import com.tgfcodes.tgfdelivery.courier.management.infrastructure.event.DeliveryFulFilledIntegrationEvent;
import com.tgfcodes.tgfdelivery.courier.management.infrastructure.event.DeliveryPlacedIntegrationEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
@KafkaListener(topics = "deliveries.v1.events", groupId = "courier-management")
public class KafkaDeliveriesMessageHandler {

    @KafkaHandler(isDefault = true)
    public void defaultHandler(@Payload Object payload) {
        log.info("Default handler received message: {}", payload);
    }


    @KafkaHandler
    public void handle(@Payload DeliveryPlacedIntegrationEvent event) {
        log.info("Received delivery placed event: {}", event);
    }

    @KafkaHandler
    public void handle(@Payload DeliveryFulFilledIntegrationEvent event) {
        log.info("Received delivery fulfilled event: {}", event);
    }
}
