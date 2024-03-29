package com.lasrosas.iot.core.ingestor.lora.api;

import java.util.ArrayList;

import org.springframework.messaging.Message;

import com.lasrosas.iot.core.ingestor.parsers.api.ThingEncodedMessage;

public interface LoraService {

	class HandleUplinkResult {
		private final Message<ThingEncodedMessage> thingEncodedMessage;
		private final Message<LoraMetricMessage> loraMetricMessage;

		public HandleUplinkResult(Message<ThingEncodedMessage> thingEncodedMessage, Message<LoraMetricMessage> loraMetricMessage) {
			super();
			this.thingEncodedMessage = thingEncodedMessage;
			this.loraMetricMessage = loraMetricMessage;
		}
		public Message<ThingEncodedMessage> getThingEncodedMessage() {
			return thingEncodedMessage;
		}
		public Message<LoraMetricMessage> getLoraMetricMessage() {
			return loraMetricMessage;
		}
	}

	ArrayList<Message<?>> splitMessage(Message<?> imessage);

	HandleUplinkResult splitUplink(Message<LoraMessageUplink> message);
}
