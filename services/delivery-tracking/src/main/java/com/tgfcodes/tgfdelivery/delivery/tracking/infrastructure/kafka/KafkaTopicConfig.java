package com.tgfcodes.tgfdelivery.delivery.tracking.infrastructure.kafka;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaTopicConfig {

    public static final String DELIVERIES_V_1_EVENTS = "deliveries.v1.events";

    @Bean
    public NewTopic deliveryEventsTopic() {
        return TopicBuilder.name(DELIVERIES_V_1_EVENTS)
                .partitions(3)
                .replicas(1)
                .build();
    }
}