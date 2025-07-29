package com.example.config_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.test.context.TestConfiguration;

@TestConfiguration(proxyBeanMethods = false)
public class TestConfigServiceApplication {

    public static void main(String[] args) {
        SpringApplication.from(ConfigServiceApplication::main).with(TestConfigServiceApplication.class).run(args);
    }

}
