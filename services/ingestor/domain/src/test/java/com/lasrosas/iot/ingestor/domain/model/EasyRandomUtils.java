package com.lasrosas.iot.ingestor.domain.model;

import org.jeasy.random.EasyRandomParameters;

import java.time.LocalDateTime;

public class EasyRandomUtils {

    public static EasyRandomParameters parameters() {
        var parameters = new EasyRandomParameters();
        parameters.randomize(LocalDateTime.class, LocalDateTime::now);
        parameters.excludeField(field -> field.getName().equals("techid"));

        return parameters;
    }
}
