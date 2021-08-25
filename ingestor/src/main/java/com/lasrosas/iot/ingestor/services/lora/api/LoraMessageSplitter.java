package com.lasrosas.iot.ingestor.services.lora.api;

import java.util.ArrayList;

import org.springframework.integration.splitter.AbstractMessageSplitter;
import org.springframework.messaging.Message;

public class LoraMessageSplitter  extends AbstractMessageSplitter {
	private final LoraService service;

	public LoraMessageSplitter(LoraService service) {
		this.service = service;
	}

	@Override
	public Object splitMessage(Message<?> imessage) {
		var result = new ArrayList<Message<?>>();
		var payload = (LoraMessage)imessage.getPayload();

		if(payload instanceof LoraMessageUplink ) {

			@SuppressWarnings("unchecked")
			var splitResult = service.splitUplink((Message<LoraMessageUplink>)imessage);

			result.add(splitResult.getLoraMetricMessage());
			result.add(splitResult.getThingEncodedMessage());
		}

		return result;
	}

	public LoraService getService() {
		return service;
	}
}
