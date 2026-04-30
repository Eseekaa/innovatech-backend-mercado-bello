package com.innovatech.bff;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

// Clase de configuración de Spring
// Patrón Factory Method: Spring crea y administra el RestTemplate
@Configuration
public class BffConfig {

    // @Bean registra RestTemplate para que Spring lo inyecte donde se necesite
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}