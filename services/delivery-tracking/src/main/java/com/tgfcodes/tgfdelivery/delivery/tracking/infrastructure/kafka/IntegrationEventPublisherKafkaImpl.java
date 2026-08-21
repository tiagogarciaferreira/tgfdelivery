package com.tgfcodes.tgfdelivery.delivery.tracking.infrastructure.kafka;

import com.tgfcodes.tgfdelivery.delivery.tracking.infrastructure.event.IntegrationEventPublisher;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NullMarked;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;

@Slf4j
@NullMarked
@RequiredArgsConstructor
@Component
public class IntegrationEventPublisherKafkaImpl implements IntegrationEventPublisher {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Override
    public void publish(Object event, String key, String topic) {
        SendResult<String, Object> sendResult = kafkaTemplate.send(topic, key, event).join();
        log.info("Message published to topic {} with key {} offset {}", topic, key, sendResult.getRecordMetadata().offset());
    }
}