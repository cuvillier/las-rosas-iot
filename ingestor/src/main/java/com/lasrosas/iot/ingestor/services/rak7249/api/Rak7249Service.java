package com.lasrosas.iot.ingestor.services.rak7249.api;

import org.springframework.messaging.Message;

import com.lasrosas.iot.ingestor.services.lora.api.LoraMessageAck;
import com.lasrosas.iot.ingestor.services.lora.api.LoraMessageJoin;
import com.lasrosas.iot.ingestor.services.lora.api.LoraMessageUplink;

public interface Rak7249Service {
	Rak7249Message fromJson(String topic, String json);

	Message<LoraMessageUplink> convertRxToLoraMessage(Message<Rak7249MessageRx> message);
	Message<LoraMessageJoin> convertJoinToLoraMessage(Message<Rak7249MessageJoin> message);
	Message<LoraMessageAck> convertAckToLoraMessage(Message<Rak7249MessageAck> message);
}
