package com.lasrosas.iot.ingestor.services.lora.api;

import com.lasrosas.iot.ingestor.services.sensors.api.ThingEncodedMessage;

public interface LoraService {

	class HandleUplinkResult {
		private final ThingEncodedMessage thingEncodedMessage;
		private final LoraMetricMessage loraMetricMessage;

		public HandleUplinkResult(ThingEncodedMessage thingEncodedMessage, LoraMetricMessage loraMetricMessage) {
			super();
			this.thingEncodedMessage = thingEncodedMessage;
			this.loraMetricMessage = loraMetricMessage;
		}
		public ThingEncodedMessage getThingEncodedMessage() {
			return thingEncodedMessage;
		}
		public LoraMetricMessage getLoraMetricMessage() {
			return loraMetricMessage;
		}
	}

	HandleUplinkResult splitUplink(LoraMessageUplink message);
	void splitJoin(LoraMessageJoin message);
}
