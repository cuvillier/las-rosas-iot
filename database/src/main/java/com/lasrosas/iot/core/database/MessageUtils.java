package com.lasrosas.iot.core.database;

import java.time.LocalDateTime;

import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;

import com.lasrosas.iot.core.database.entities.dtw.DigitalTwin;
import com.lasrosas.iot.core.database.entities.thg.Thing;
import com.lasrosas.iot.core.shared.utils.LasRosasHeaders;

public class MessageUtils {

	public static <T> MessageBuilder<T> buildMessage(Message<?> imessage, T payload) {

		var builder = MessageBuilder.withPayload(payload);

		LocalDateTime time = null;

		if( imessage != null)  time = LasRosasHeaders.timeReceived(imessage);
		if( time == null ) time = LocalDateTime.now();
		builder = builder.setHeader(LasRosasHeaders.TIME_RECEIVED, time);

		return builder;
	}

	public static <T> MessageBuilder<T> buildMessage(Message<?> imessage, Thing thing, T payload) {

		var builder = MessageBuilder.withPayload(payload);

		LocalDateTime time = null;

		if( imessage != null)  time = LasRosasHeaders.timeReceived(imessage);
		if( time == null ) time = LocalDateTime.now();
		builder = builder.setHeader(LasRosasHeaders.TIME_RECEIVED, time);

		if(thing != null) {
			builder = builder.setHeader(LasRosasHeaders.THING_ID, thing.getTechid());
			builder = builder.setHeader(LasRosasHeaders.THING_NATURAL_ID, thing.getNaturalId());
		}

		return builder;
	}

	public static <T> MessageBuilder<T> buildMessage(Message<?> imessage, DigitalTwin twin, T payload) {

		var builder = MessageBuilder.withPayload(payload);

		LocalDateTime time = null;

		if( imessage != null)  time = LasRosasHeaders.timeReceived(imessage);
		if( time == null ) time = LocalDateTime.now();
		builder = builder.setHeader(LasRosasHeaders.TIME_RECEIVED, time);

		if(twin != null) {
			builder = builder.setHeader(LasRosasHeaders.TWIN_ID, twin.getTechid());
			builder = builder.setHeader(LasRosasHeaders.TWIN_NATURAL_ID, twin.getName());
		}

		return builder;
	}
}
