package com.lasrosas.iot.services.utils;

import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;

@Configuration
public class UtilsConfig {

	/*
	@Bean
    public ObjectMapper customJson() {

        return new Jackson2ObjectMapperBuilder()
            .indentOutput(true)
            .propertyNamingStrategy(PropertyNamingStrategy.UPPER_CAMEL_CASE)
            .build();
    }
    */

	@Bean
	public Jackson2ObjectMapperBuilderCustomizer customJson()
	{
	    return builder -> {

	        builder.indentOutput(true);
	        builder.propertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE);
	    };
	}
}