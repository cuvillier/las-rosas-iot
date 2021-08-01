package com.lasrosas.iot.ingestor.services.rak7249.api;

import com.lasrosas.iot.ingestor.services.lora.api.LoraMessageAck;
import com.lasrosas.iot.ingestor.services.lora.api.LoraMessageJoin;
import com.lasrosas.iot.ingestor.services.lora.api.LoraMessageUplink;

public interface Rak7249Service {
	Rak7249Message fromJson(String topic, String json);

	LoraMessageUplink convertRxToLoraMessage(Rak7249MessageRx message);
	LoraMessageJoin convertJoinToLoraMessage(Rak7249MessageJoin message);
	LoraMessageAck convertAckToLoraMessage(Rak7249MessageAck message);
}
