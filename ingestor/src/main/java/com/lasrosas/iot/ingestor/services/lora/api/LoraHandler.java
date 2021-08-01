package com.lasrosas.iot.ingestor.services.lora.api;

import org.springframework.integration.context.IntegrationObjectSupport;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;

public class LoraHandler  extends IntegrationObjectSupport {
	private final MessageChannel loraMetricChannel;
	private final  MessageChannel thingEncodedChannel;
	private final LoraService service;

	public LoraHandler(MessageChannel loraMetricChannel, MessageChannel thingEncodedChannel, LoraService service) {
		this.loraMetricChannel = loraMetricChannel;
		this.thingEncodedChannel = thingEncodedChannel;
		this.service = service;
	}

	public void handleEvent(Message<? extends LoraMessage> imessage) {
		var payload = imessage.getPayload();

		if(payload instanceof LoraMessageUplink ) {
			var result = service.handleUplink((LoraMessageUplink) payload);

			// Send result
			if( result.getLoraMetricMessage() != null) {
				var message = getMessageBuilderFactory().withPayload(result.getLoraMetricMessage()).copyHeaders(imessage.getHeaders());
				loraMetricChannel.send((Message<?>)message);
			}

			if( result.getThingEncodedMessage() != null) {
				var message = getMessageBuilderFactory().withPayload(result.getThingEncodedMessage()).copyHeaders(imessage.getHeaders());
				thingEncodedChannel.send((Message<?>)message);
			}
		}
	}

	public MessageChannel getLoraMetricChannel() {
		return loraMetricChannel;
	}

	public MessageChannel getThingEncodedChannel() {
		return thingEncodedChannel;
	}

	public LoraService getService() {
		return service;
	}
}
