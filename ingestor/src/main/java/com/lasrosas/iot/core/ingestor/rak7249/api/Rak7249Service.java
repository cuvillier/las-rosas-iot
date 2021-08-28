package com.lasrosas.iot.core.ingestor.rak7249.api;

import org.springframework.messaging.Message;

import com.lasrosas.iot.core.ingestor.lora.api.LoraMessageAck;
import com.lasrosas.iot.core.ingestor.lora.api.LoraMessageJoin;
import com.lasrosas.iot.core.ingestor.lora.api.LoraMessageUplink;

public interface Rak7249Service {
	Rak7249Message fromJson(String topic, String json);

	Message<LoraMessageUplink> convertRxToLoraMessage(Message<Rak7249MessageRx> message);
	Message<LoraMessageJoin> convertJoinToLoraMessage(Message<Rak7249MessageJoin> message);
	Message<LoraMessageAck> convertAckToLoraMessage(Message<Rak7249MessageAck> message);
}
