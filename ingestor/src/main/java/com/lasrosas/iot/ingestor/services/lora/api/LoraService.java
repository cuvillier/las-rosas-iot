package com.lasrosas.iot.ingestor.services.lora.api;

import org.springframework.messaging.Message;

import com.lasrosas.iot.ingestor.services.sensors.api.ThingEncodedMessage;

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

	HandleUplinkResult splitUplink(Message<LoraMessageUplink> message);
	void splitJoin(Message<LoraMessageJoin> message);
}
