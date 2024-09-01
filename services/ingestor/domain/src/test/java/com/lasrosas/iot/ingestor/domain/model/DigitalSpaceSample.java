package com.lasrosas.iot.ingestor.domain.model;

import com.lasrosas.iot.ingestor.domain.model.digitalTwin.DigitalSpace;
import com.lasrosas.iot.ingestor.domain.model.thing.ThingType;
import org.jeasy.random.EasyRandom;
import org.junit.jupiter.api.AssertionFailureBuilder;
import org.junit.jupiter.api.Assertions;

public class DigitalSpaceSample {
    public static DigitalSpace defaultSpace = DigitalSpace.builder().name("spacename").build();

    public static DigitalSpace sample() {
        var parameters = EasyRandomUtils.parameters();
        return new EasyRandom(parameters).nextObject(DigitalSpace.class);
    }

    public static void assertEquals(DigitalSpace expected, DigitalSpace actual) {
        if((expected == null) != (actual == null)) AssertionFailureBuilder.assertionFailure().message("expected or actual is null").expected(expected).actual(actual).buildAndThrow();
        if(expected == null || actual == null) return;

        Assertions.assertEquals(expected.getName(), actual.getName());
    }
}
