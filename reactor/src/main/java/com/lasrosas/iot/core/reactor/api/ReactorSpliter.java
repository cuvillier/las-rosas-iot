package com.lasrosas.iot.core.reactor.api;

import org.springframework.integration.splitter.AbstractMessageSplitter;
import org.springframework.messaging.Message;

public class ReactorSpliter extends AbstractMessageSplitter {

	private ReactorService service;

	public ReactorSpliter(ReactorService service) {
		this.service = service;
	}

	/*
	 * Decode Message from a Lora sensor, and normalize the data model.
	 * return a collection of ThingMessages
	 */
	@Override
	protected Object splitMessage(Message<?> imessage) {

		var result = service.react(imessage);
		return result;
	}
}
