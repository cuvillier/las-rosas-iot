package com.lasrosas.iot.ingestor.domain.model;

import com.lasrosas.iot.ingestor.domain.model.thing.ThingType;
import org.jeasy.random.EasyRandom;
import org.junit.jupiter.api.AssertionFailureBuilder;
import org.junit.jupiter.api.Assertions;

public class ThingTypeSample {
    public static ThingType Adeunis_ARF8180BA = ThingType.builder()
                .techid(123456L)
                .manufacturer("Adeunis")
                .model("ARF8180BA")
                .readable("Adeunis-ARF8180BA")
                .batteryMinPercentage(0.22)
                .build();

    public static ThingType Adeunis_ARF8170BA = ThingType.builder()
            .techid(123456L)
            .manufacturer("Adeunis")
            .model("ARF8170BA")
            .readable("Adeunis-ARF8170BA")
            .batteryMinPercentage(0.22)
            .build();

    public static ThingType DRAGINO_LHT65 = ThingType.builder()
            .techid(223456L)
            .manufacturer("DRAGINO")
            .model("LHT65")
            .readable("DRAGINO-LHT65")
            .batteryMinPercentage(0.22)
            .build();

    public static ThingType Elsys_ERS = ThingType.builder()
            .techid(323456L)
            .manufacturer("Elsys")
            .model("ERS")
            .readable("Elsys-ERS")
            .batteryMinPercentage(0.22)
            .build();

    public static ThingType Elsys_MB7389 = ThingType.builder()
            .techid(423456L)
            .manufacturer("Elsys")
            .model("MB7389")
            .readable("Elsys-MB7389")
            .batteryMinPercentage(0.22)
            .build();

    public static ThingType MFC88_LW13IO70 = ThingType.builder()
            .techid(523456L)
            .manufacturer("MFC88")
            .model("LW13IO70")
            .readable("MFC88-LW13IO70")
            .batteryMinPercentage(0.22)
            .build();

    public static ThingType sample() {
        var parameters = EasyRandomUtils.parameters();
        return new EasyRandom(parameters).nextObject(ThingType.class);
    }

    public static void assertEquals(ThingType expected, ThingType actual) {
        if((expected == null) != (actual == null)) AssertionFailureBuilder.assertionFailure().message("expected or actual is null").expected(expected).actual(actual).buildAndThrow();
        if(expected == null || actual == null) return;

        Assertions.assertEquals(expected.getReadable(), actual.getReadable());
        Assertions.assertEquals(expected.getModel(), actual.getModel());
        Assertions.assertEquals(expected.getBatteryMinPercentage(), actual.getBatteryMinPercentage());
        Assertions.assertEquals(expected.getManufacturer(), actual.getManufacturer());
    }
}
