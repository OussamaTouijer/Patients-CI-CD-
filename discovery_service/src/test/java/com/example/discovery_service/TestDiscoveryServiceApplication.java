package com.example.discovery_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.test.context.TestConfiguration;

@TestConfiguration(proxyBeanMethods = false)
public class TestDiscoveryServiceApplication {

    public static void main(String[] args) {
        SpringApplication.from(DiscoveryServiceApplication::main).with(TestDiscoveryServiceApplication.class).run(args);
    }

}
