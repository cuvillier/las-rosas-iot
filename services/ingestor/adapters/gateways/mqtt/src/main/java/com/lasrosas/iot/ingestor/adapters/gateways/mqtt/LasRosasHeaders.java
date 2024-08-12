package com.lasrosas.iot.ingestor.adapters.gateways.mqtt;

import org.springframework.messaging.Message;

import java.time.LocalDateTime;
import java.util.Optional;

public class LasRosasHeaders {
	public static String THING_ID = "ThingId";
	public static String THING_NATURAL_ID = "ThingNaturalId";
	public static String SENSOR = "Sensor";
	public static String TWIN_ID = "TwinId";
	public static String TWIN_NATURAL_ID = "TwinNaturalId";
	public static String TIME_RECEIVED = "TimeReceived";
	public static String ORIGIN_ID = "OriginId";
	public static String ORIGIN_TYPE = "OriginType";
	public static String ORIGIN_THING = "thg";
	public static String ORIGIN_TWIN = "twi";
	public static String TOPIC = "topic";

	/* Copy of IntegrationMessageHeaderAccessor */
	public static final String CORRELATION_ID = "correlationId";
	public static final String EXPIRATION_DATE = "expirationDate";
	public static final String PRIORITY = "priority";
	public static final String SEQUENCE_NUMBER = "sequenceNumber";
	public static final String SEQUENCE_SIZE = "sequenceSize";
	public static final String SEQUENCE_DETAILS = "sequenceDetails";
	public static final String ROUTING_SLIP = "routingSlip";
	public static final String DUPLICATE_MESSAGE = "duplicateMessage";
	public static final String CLOSEABLE_RESOURCE = "closeableResource";
	public static final String DELIVERY_ATTEMPT = "deliveryAttempt";
	public static final String ACKNOWLEDGMENT_CALLBACK = "acknowledgmentCallback";
	public static final String GATEWAY_NAURAL_ID = "gatewayNaturalId";

	public static String sensor(Message<?> message) {
		return message.getHeaders().get(SENSOR, String.class);
	}

	public static LocalDateTime timeReceived(Message<?> message) {
		return message.getHeaders().get(TIME_RECEIVED, LocalDateTime.class);
	}

	public static Optional<Long> thingId(Message<?> message) {
		return Optional.ofNullable(message.getHeaders().get(THING_ID, Long.class));
	}

	public static Optional<String> thingNaturalId(Message<?> message) {
		return Optional.ofNullable(message.getHeaders().get(THING_NATURAL_ID, String.class));
	}

	public static Optional<Long> twinId(Message<?> message) {
		return Optional.ofNullable(message.getHeaders().get(TWIN_ID, Long.class));
	}

	public static Optional<String> gatewayNaturalId(Message<?> message) {
		return Optional.ofNullable(message.getHeaders().get(GATEWAY_NAURAL_ID, String.class));
	}
	public static Optional<String> topic(Message<?> message) {
		return Optional.ofNullable(message.getHeaders().get(TOPIC, String.class));
	}

	public static Optional<String> twinNaturalId(Message<?> message) {
		return Optional.ofNullable(message.getHeaders().get(TWIN_NATURAL_ID, String.class));
	}

	public static Optional<String> naturalId(Message<?> message) {
		var thingNaturalId = thingNaturalId(message);

		if( thingNaturalId.isEmpty())
			return twinNaturalId(message);

		return thingNaturalId;
	}

	public static Optional<String> schema(Message<?> message) {
		return Optional.ofNullable(message.getPayload().getClass().getSimpleName());
	}

	public static String correlationId(Message<?> message) {
		var correlationId = message.getHeaders().get(CORRELATION_ID, Object.class);
		return correlationId == null?null: correlationId.toString();
	}
}
