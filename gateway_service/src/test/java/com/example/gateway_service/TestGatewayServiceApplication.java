package com.example.gateway_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.test.context.TestConfiguration;

@TestConfiguration(proxyBeanMethods = false)
public class TestGatewayServiceApplication {

    public static void main(String[] args) {
        SpringApplication.from(GatewayServiceApplication::main).with(TestGatewayServiceApplication.class).run(args);
    }

}
