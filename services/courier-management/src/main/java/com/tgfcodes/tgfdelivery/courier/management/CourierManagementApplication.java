package com.tgfcodes.tgfdelivery.courier.management;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class CourierManagementApplication {

    void main(String[] args) {
        SpringApplication.run(CourierManagementApplication.class, args);
    }
}