package com.lasrosas.iot.core.ingestor.gateway.impl.rak7249.api;

import org.springframework.messaging.Message;

import com.lasrosas.iot.core.ingestor.gateway.api.GatewayDriver;
import com.lasrosas.iot.core.ingestor.lora.api.LoraMessage;
import com.lasrosas.iot.core.ingestor.lora.api.LoraMessageAck;
import com.lasrosas.iot.core.ingestor.lora.api.LoraMessageJoin;
import com.lasrosas.iot.core.ingestor.lora.api.LoraMessageUplink;

public interface Rak7249Driver extends GatewayDriver {
	String DRIVER_NAME = "Rak7249";
	Rak7249Message fromJson(String topic, String json);

	Message<? extends LoraMessage> transform(Message<? extends Rak7249Message> message);
	Message<LoraMessageUplink> convertRxToLoraMessage(Message<Rak7249MessageRx> message);
	Message<LoraMessageJoin> convertJoinToLoraMessage(Message<Rak7249MessageJoin> message);
	Message<LoraMessageAck> convertAckToLoraMessage(Message<Rak7249MessageAck> message);
}
