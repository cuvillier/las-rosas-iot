package com.lasrosas.iot.ingestor.domain.model;

import com.lasrosas.iot.ingestor.domain.model.thing.*;
import org.jeasy.random.EasyRandom;
import org.junit.jupiter.api.AssertionFailureBuilder;
import org.junit.jupiter.api.Assertions;

public class ThingSample {

    // Data from insert-test-data.sql
    public static final Thing Test_Elsys_MB7389 = Thing.builder()
            .techid(108982982L)
            .naturalid("0100000000000001")
            .adminState(Thing.AdminState.CONNECTED)
            .proxy(ThingProxySample.sample())
            .connectionTimeout(12345)
            .gateway(ThingGatewaySample.TEST_GATEWAY)
            .readable("Test Elsys MB7389")
            .type(ThingTypeSample.Elsys_MB7389)
            .build();

    public static final Thing Test_ELSYS_ERS = Thing.builder()
            .techid(208982982L)
            .naturalid("0200000000000001")
            .adminState(Thing.AdminState.CONNECTED)
            .proxy(ThingProxySample.sample())
            .connectionTimeout(12345)
            .gateway(ThingGatewaySample.TEST_GATEWAY)
            .readable("Test ELSYS ERS")
            .type(ThingTypeSample.Elsys_ERS)
            .build();

    public static final Thing Test_Adeunis_ARF8180BA = Thing.builder()
            .techid(308982982L)
            .naturalid("0300000000000001")
            .adminState(Thing.AdminState.CONNECTED)
            .proxy(ThingProxySample.sample())
            .connectionTimeout(12345)
            .gateway(ThingGatewaySample.TEST_GATEWAY)
            .readable("Test Adeunis ARF8180BA")
            .type(ThingTypeSample.Adeunis_ARF8180BA)
            .build();

    public static final Thing Test_Adeunis_ARF8170BA = Thing.builder()
            .techid(408982982L)
            .naturalid("0300000000000002")
            .adminState(Thing.AdminState.CONNECTED)
            .proxy(ThingProxySample.sample())
            .connectionTimeout(12345)
            .gateway(ThingGatewaySample.TEST_GATEWAY)
            .readable("Test Adeunis ARF8180BA")
            .type(ThingTypeSample.Adeunis_ARF8180BA)
            .build();

    public static final Thing Test_DRAGINO_LHT65 = Thing.builder()
            .techid(508982982L)
            .naturalid("0400000000000001")
            .adminState(Thing.AdminState.CONNECTED)
            .proxy(ThingProxySample.sample())
            .connectionTimeout(12345)
            .gateway(ThingGatewaySample.TEST_GATEWAY)
            .readable("Test DRAGINO LHT65")
            .type(ThingTypeSample.DRAGINO_LHT65)
            .build();

    public static Thing sample() {
        var parameters = EasyRandomUtils.parameters();
        parameters.randomize(ThingProxy.class, ThingProxySample::sample);
        parameters.randomize(ThingGateway.class, ThingGatewaySample::sample);
        parameters.randomize(ThingType.class, ThingTypeSample::sample);
        return new EasyRandom(parameters).nextObject(Thing.class);
    }

    public static Thing sample(ThingGateway gateway, ThingType type) {
        var parameters = EasyRandomUtils.parameters();
        var domain = new EasyRandom(parameters).nextObject(Thing.class);
        domain.setGateway(gateway);
        domain.setType(type);

        return domain;
    }

    public static void assertEquals(Thing expected, Thing actual) {
        if((expected == null) != (actual == null)) AssertionFailureBuilder.assertionFailure().message("expected or actual is null").expected(expected).actual(actual).buildAndThrow();
        if(expected == null || actual == null) return;

        Assertions.assertEquals(expected.getNaturalid(), actual.getNaturalid());
        Assertions.assertEquals(expected.getAdminState(), actual.getAdminState());
        Assertions.assertEquals(expected.getReadable(), actual.getReadable());
        Assertions.assertEquals(expected.getConnectionTimeout(), actual.getConnectionTimeout());

        ThingGatewaySample.assertEquals(expected.getGateway(), actual.getGateway());
        ThingTypeSample.assertEquals(expected.getType(), actual.getType());

        // Proxy is dynamic, null by default.
        // ThingProxySample.assertEquals(expected.getProxy(), actual.getProxy());
    }

}
