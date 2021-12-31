package com.lasrosas.iot.core.ingestor.lora.api;

import org.springframework.messaging.Message;

import com.lasrosas.iot.core.ingestor.sensors.api.ThingEncodedMessage;
import com.lasrosas.iot.core.shared.telemetry.ThingConnectionState;

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

	default ThingConnectionState splitJoin(Message<LoraMessageJoin> message) {
		return null;
	}
}
