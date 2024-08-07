package com.lasrosas.iot.ingestor.usecases.handleLorawanMessages.thingDrivers;

import com.lasrosas.iot.ingestor.shared.exceptions.NotFoundException;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@AllArgsConstructor
public class ThingDriverManager {

    @Getter
    @AllArgsConstructor
    @Builder
    @EqualsAndHashCode
    private static class DriverKey {
        private String manufacturer;
        private String model;
    }

    private Map<DriverKey, ThingDriver> drivers;

    public ThingDriverManager(ThingDriver ... driverArray) {

        drivers = new HashMap<>();
        for(var driver: driverArray) {
            var key = DriverKey.builder().manufacturer(driver.getManufacturer()).model(driver.getModel()).build();
            drivers.put(key, driver);
        }
    }

    public ThingDriver get(String manufacturer, String model) {
        var key = DriverKey.builder().manufacturer(manufacturer).model(model).build();
        var driver = drivers.get(key);

        if(driver == null)
            throw new NotFoundException("Thing driver manufacturer=" + manufacturer + ", model=" + model);

        return driver;
    }
}
