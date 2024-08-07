package com.lasrosas.iot.ingestor.usecases.handleLorawanMessages.gatewayDrivers;

import com.lasrosas.iot.ingestor.usecases.handleLorawanMessages.gatewayDrivers.rak.RAKGatewayDriver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LorawanGatewayConfig {

    @Bean
    public RAKGatewayDriver RAKGatewayDriver() {
        return RAKGatewayDriver.builder().build();
    }

    @Bean
    public LorawanGatewayDriverManager LorawanGatewayDriverManager(RAKGatewayDriver raKGatewayDriver) {
        return new LorawanGatewayDriverManager(raKGatewayDriver);
    }
}
