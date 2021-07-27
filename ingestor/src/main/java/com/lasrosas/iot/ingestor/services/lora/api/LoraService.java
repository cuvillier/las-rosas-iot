package com.lasrosas.iot.ingestor.services.lora.api;

import com.lasrosas.iot.ingestor.shared.ThingMessage;

public interface LoraService {
	ThingMessage handleUpload(LoraMessageUpload uploadMessage);
	public void handleJoin(LoraMessageJoin joinMessage);
}
