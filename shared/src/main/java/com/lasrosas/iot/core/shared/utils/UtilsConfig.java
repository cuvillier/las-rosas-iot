package com.lasrosas.iot.core.shared.utils;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.LongSerializationPolicy;

import io.goodforgod.gson.configuration.GsonConfiguration;

@Configuration
public class UtilsConfig {

	@Bean
	public GsonBuilder gsonBuilder() {
		return GsonUtils.gsonBuilder();
	}

	@Bean
	@Scope("prototype")
	public Gson gson(GsonBuilder builder) {
		GsonBuilder n = null;
		return builder.create();
	}
}
