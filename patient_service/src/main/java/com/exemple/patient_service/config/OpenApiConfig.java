package com.exemple.patient_service.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("API Service Patient")
                        .description("API REST pour la gestion des patients dans un système de microservices")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Équipe Développement")
                                .email("dev@example.com")
                                .url("https://github.com/example/patient-service"))
                        .license(new License()
                                .name("MIT License")
                                .url("https://opensource.org/licenses/MIT")))
                .servers(List.of(
                        new Server()
                                .url("http://localhost:9006")
                                .description("Serveur de développement"),
                        new Server()
                                .url("http://gateway-service:8080")
                                .description("Serveur via API Gateway")
                ));
    }
} 