package com.lasrosas.iot.core.reactor.api;

import org.springframework.integration.splitter.AbstractMessageSplitter;
import org.springframework.messaging.Message;

import com.lasrosas.iot.core.shared.telemetry.Telemetry;

public class ReactorSpliter extends AbstractMessageSplitter {

	private ReactorService service;

	public ReactorSpliter(ReactorService service) {
		this.service = service;
	}

	/*
	 * Decode Message from a Lora sensor, and normalize the data model.
	 * return a collection of ThingMessages
	 */
	@SuppressWarnings("unchecked")
	@Override
	protected Object splitMessage(Message<?> imessage) {

		if(imessage.getPayload() instanceof Telemetry) {
			var result = service.react((Message<? extends Telemetry>)imessage);
			return result;
		}

		return null;
	}
}
