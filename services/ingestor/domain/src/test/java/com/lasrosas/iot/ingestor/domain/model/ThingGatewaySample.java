package com.lasrosas.iot.ingestor.domain.model;

import com.lasrosas.iot.ingestor.domain.model.thing.ThingGateway;
import org.jeasy.random.EasyRandom;
import org.junit.jupiter.api.AssertionFailureBuilder;
import org.junit.jupiter.api.Assertions;

public class ThingGatewaySample {

    public static final ThingGateway TEST_GATEWAY = ThingGateway.builder()
                .techid(123456234434L)
                .naturalid("TestGateway")
                .build();

    public static ThingGateway sample() {
        var parameters = EasyRandomUtils.parameters();
        return new EasyRandom(parameters).nextObject(ThingGateway.class);
    }

    public static void assertEquals(ThingGateway expected, ThingGateway actual) {
        if((expected == null) != (actual == null)) AssertionFailureBuilder.assertionFailure().message("expected or actual is null").expected(expected).actual(actual).buildAndThrow();
        if(expected == null || actual == null) return;

        Assertions.assertEquals(expected.getNaturalid(), actual.getNaturalid());
    }
}
