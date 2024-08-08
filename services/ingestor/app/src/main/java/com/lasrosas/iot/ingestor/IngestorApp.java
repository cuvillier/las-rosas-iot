package com.lasrosas.iot.ingestor;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.AutoConfigurationPackage;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@EnableAutoConfiguration
@ComponentScan(basePackages = "com.lasrosas.iot.ingestor")
@AutoConfigurationPackage(basePackages = {"com.lasrosas.iot.ingestor"})
@RestController
public class IngestorApp {

    public static void main(String[] args) {
        SpringApplication.run(IngestorApp.class, args);
    }

    @GetMapping
    public String home() {
        return "Las Rosas IOT";
    }
}
