package com.lasrosas.iot.core.shared.utils;

import java.time.LocalDateTime;

import org.springframework.messaging.Message;

public class LasRosasHeaders {
	public static String THING_ID = "ThingId";
	public static String THING_NATURAL_ID = "ThingNaturalId";
	public static String TWIN_ID = "TwinId";
	public static String TWIN_NATURAL_ID = "TwinNaturalId";
	public static String TIME_RECEIVED = "TimeReceived";
	public static String SENSOR = "Sensor";

	public static String sensor(Message<?> message) {
		return message.getHeaders().get(SENSOR, String.class);
	}

	public static LocalDateTime time(Message<?> message) {
		return message.getHeaders().get(TIME_RECEIVED, LocalDateTime.class);
	}

	public static Long thingid(Message<?> message) {
		return message.getHeaders().get(THING_ID, Long.class);
	}

	public static String thingNaturalId(Message<?> message) {
		return message.getHeaders().get(THING_NATURAL_ID, String.class);
	}

	public static Long twinId(Message<?> message) {
		return message.getHeaders().get(TWIN_ID, Long.class);
	}

	public static String twinNaturalId(Message<?> message) {
		return message.getHeaders().get(TWIN_NATURAL_ID, String.class);
	}

	public static String schema(Message<?> message) {
		return message.getPayload().getClass().getSimpleName();
	}
}
