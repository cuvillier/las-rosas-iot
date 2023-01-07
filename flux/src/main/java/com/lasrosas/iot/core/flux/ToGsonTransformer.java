package com.lasrosas.iot.core.flux;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.transformer.AbstractTransformer;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;

import com.google.gson.Gson;
import com.lasrosas.iot.core.shared.utils.LasRosasHeaders;

public class ToGsonTransformer extends AbstractTransformer {

	@Autowired
	private Gson gson;

	@Override
	protected Object doTransform(Message<?> message) {
		var json = "{\n" 
		   + "  \"schema\": \"" + message.getPayload().getClass().getSimpleName() + "\",\n"
	       + "  \"payload\":" + gson.toJson(message.getPayload()) + "\n"
	       + "}";

		String topic;

		if( LasRosasHeaders.twinNaturalId(message).isPresent() ) {
			topic = "twin/" + LasRosasHeaders.twinId(message).get();
		} else if( LasRosasHeaders.thingNaturalId(message).isPresent() ) {
			topic = "thing/" + LasRosasHeaders.thingid(message).get();
		} else
			throw new RuntimeException("Unknow naruralId for this message");

		topic += "/uplink/telemetry";

		return MessageBuilder.withPayload(json)
				.copyHeaders(message.getHeaders())
				.setHeader(LasRosasHeaders.TOPIC, topic)
				.build();
	}
}
