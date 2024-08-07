package com.lasrosas.iot.ingestor.domain.model;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.lasrosas.iot.ingestor.shared.JsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

@Slf4j
public class SampleTest {

    @Test
    public void ThingGatewaySample() {
        var sample = ThingGatewaySample.sample();
        printJson(sample);
    }

    private void printJson(Object o) {
        var mapper = JsonUtils.mapper();
        mapper.registerModule(new JavaTimeModule());
        var writer = mapper.writerWithDefaultPrettyPrinter();
        String json = null;
        try {
            json = writer.writeValueAsString(o);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        log.info("");
        log.info(o.getClass().getSimpleName() + ": ");
        log.info(json);
    }
    @Test
    public void ThingTypeSample() {
        var sample = ThingTypeSample.sample();
        printJson(sample);
    }

    @Test
    public void ThingLoraSample() {
        var sample = ThingSample.sample();
        printJson(sample);
    }

    @Test
    public void ThingProxySample() {
        var sample = ThingProxySample.sample();
        printJson(sample);
    }
}
