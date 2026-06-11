package com.innovatech.mstareas;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

// Clase principal: levanta el microservicio de tareas con Spring Boot.
@SpringBootApplication
public class MsTareasApplication {

    public static void main(String[] args) {
        SpringApplication.run(MsTareasApplication.class, args);
    }
}
