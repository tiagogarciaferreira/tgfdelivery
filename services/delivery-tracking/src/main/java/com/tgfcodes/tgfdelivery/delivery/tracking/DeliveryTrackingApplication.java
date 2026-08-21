package com.tgfcodes.tgfdelivery.delivery.tracking;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class DeliveryTrackingApplication {

    void main(String[] args) {
        SpringApplication.run(DeliveryTrackingApplication.class, args);
    }
}