package com.lasrosas.iot.ingestor;

import com.lasrosas.iot.ingestor.domain.ports.eventsources.IngestorMessagePublisher;
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
        var context = SpringApplication.run(IngestorApp.class, args);

        var mqtt = context.getBean(IngestorMessagePublisher.class);
        mqtt.send("lasrosasiot/ingestor", "started");
    }

    @GetMapping
    public String home() {
        return "Las Rosas IOT";
    }
}
