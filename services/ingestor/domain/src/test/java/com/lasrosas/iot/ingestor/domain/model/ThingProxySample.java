package com.lasrosas.iot.ingestor.domain.model;

import com.lasrosas.iot.ingestor.domain.model.thing.ThingProxy;
import org.jeasy.random.EasyRandom;
import org.junit.jupiter.api.AssertionFailureBuilder;
import org.junit.jupiter.api.Assertions;

public class ThingProxySample {

    public static ThingProxy sample() {
        var parameters = EasyRandomUtils.parameters();
        return new EasyRandom(parameters).nextObject(ThingProxy.class);
    }

    public static void assertEquals(ThingProxy expected, ThingProxy actual) {
        if((expected == null) != (actual == null)) AssertionFailureBuilder.assertionFailure().message("expected or actual is null").expected(expected).actual(actual).buildAndThrow();
        if(expected == null || actual == null) return;

        Assertions.assertEquals(expected.getBatteryLevel(), actual.getBatteryLevel());
        Assertions.assertEquals(expected.getValues(), actual.getValues());
        Assertions.assertEquals(expected.getConfig(), actual.getConfig());
        Assertions.assertEquals(expected.getLastSeen(), actual.getLastSeen());
        Assertions.assertEquals(expected.getBatteryState(), actual.getBatteryState());
        Assertions.assertEquals(expected.getConnectionState(), actual.getConnectionState());
    }
}
