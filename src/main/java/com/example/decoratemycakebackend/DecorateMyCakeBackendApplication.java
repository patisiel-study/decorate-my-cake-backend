package com.example.decoratemycakebackend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class DecorateMyCakeBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(DecorateMyCakeBackendApplication.class, args);
    }

}
