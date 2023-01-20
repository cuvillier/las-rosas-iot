package com.lasrosas.iot.alarm.service.api;

import org.springframework.context.annotation.Bean;

import com.lasrosas.iot.alarm.service.impl.AlarmServiceImpl;

public class AlarmServiceConfig {

	@Bean
	public AlarmService AlarmService() {
		return new AlarmServiceImpl();
	}
}
