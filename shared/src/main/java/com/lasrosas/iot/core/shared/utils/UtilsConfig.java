package com.lasrosas.iot.core.shared.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

@Configuration
public class UtilsConfig {
	public static final Logger log = LoggerFactory.getLogger(UtilsConfig.class);

	@Bean
	public GsonBuilder gsonBuilder() {
		return GsonUtils.gsonBuilder();
	}

	@Bean
	@Scope("prototype")
	public Gson gson(GsonBuilder builder) {
		return builder.create();
	}
}
