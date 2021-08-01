package com.lasrosas.iot.ingestor.services.lora.api;

import com.lasrosas.iot.ingestor.services.sensors.api.ThingEncodedMessage;

public interface LoraService {

	class HandleUplinkResult {
		private ThingEncodedMessage thingEncodedMessage;
		private LoraMetricMessage loraMetricMessage;

		public ThingEncodedMessage getThingEncodedMessage() {
			return thingEncodedMessage;
		}
		public void setThingEncodedMessage(ThingEncodedMessage thingEncodedMessage) {
			this.thingEncodedMessage = thingEncodedMessage;
		}
		public LoraMetricMessage getLoraMetricMessage() {
			return loraMetricMessage;
		}
		public void setLoraMetricMessage(LoraMetricMessage loraMetricMessage) {
			this.loraMetricMessage = loraMetricMessage;
		}
	}

	HandleUplinkResult handleUplink(LoraMessageUplink message);
	void handleJoin(LoraMessageJoin message);
}
