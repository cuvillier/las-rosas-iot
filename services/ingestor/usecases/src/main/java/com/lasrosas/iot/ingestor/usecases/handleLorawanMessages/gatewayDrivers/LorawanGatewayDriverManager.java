package com.lasrosas.iot.ingestor.usecases.handleLorawanMessages.gatewayDrivers;

import com.lasrosas.iot.ingestor.shared.exceptions.NotFoundException;
import lombok.AllArgsConstructor;

import java.util.*;

@AllArgsConstructor
public class LorawanGatewayDriverManager {
    private Map<String, LorawanGatewayDriver> drivers;

    public LorawanGatewayDriverManager(LorawanGatewayDriver ... driverArray) {

        drivers = new HashMap<>();
        for(var driver: driverArray) {
            drivers.put(driver.getName(), driver);
        }
    }

    public LorawanGatewayDriver get(String driverName) {
        var driver =  drivers.get(driverName);
        if(driver == null)
            throw new NotFoundException("Locarawan gateway driver for " + driverName);

        return driver;
    }
}
