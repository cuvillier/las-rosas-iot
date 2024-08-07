package com.lasrosas.iot.ingestor.usecases.handleLorawanMessages.thingDrivers.elsys;

import com.lasrosas.iot.ingestor.domain.model.message.ThingMessage;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Setter
@Getter
@SuperBuilder
public class ElsysGenericUplinkFrame extends ThingMessage {
    private Double temperature;
    private Integer humidity;
    private Integer accelerationX;
    private Integer accelerationY;
    private Integer accelerationZ;
    private Integer light;
    private Integer motion;
    private Integer co2;
    private Integer vdd;
    private Integer analog1;
    private Integer gpsLatitude;
    private Integer gpsLongitude;
    private Integer pulse1;
    private Integer pulseAbs;
    private Double externalTemperature;
    private Integer digital;
    private Integer distance;
    private Integer accMotion;
    private Double irInternalTemperature;
    private Double irExternalTemperature;
    private Integer occupancy;
    private Integer waterleak;
    private Double pressure;
    private Integer soundPeak;
    private Integer soundAvg;
    private Integer pulse2;
    private Integer pulseAbs2;
    private Integer analog2;
    private Double externalTemperature2;
    private double [] grideye;
    private Integer analogUV;
}
