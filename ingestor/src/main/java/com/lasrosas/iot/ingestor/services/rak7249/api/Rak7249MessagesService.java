package com.lasrosas.iot.ingestor.services.rak7249.api;

import org.springframework.messaging.Message;

import com.lasrosas.iot.ingestor.services.lora.api.LoraMessageAck;
import com.lasrosas.iot.ingestor.services.lora.api.LoraMessageJoin;
import com.lasrosas.iot.ingestor.services.lora.api.LoraMessageUpload;

public interface Rak7249MessagesService {
	public LoraMessageUpload handleRx(Message<Rak7249MessageRx> rxMessage);
	public LoraMessageJoin handleJoin(Message<Rak7249MessageJoin> rxMessage);
	public LoraMessageAck handleAck(Message<Rak7249MessageAck> rxMessage);
}
