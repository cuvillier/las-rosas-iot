package com.lasrosas.iot.ingestor.services.rak7249.api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.integration.transformer.AbstractTransformer;
import org.springframework.messaging.Message;

import com.lasrosas.iot.ingestor.services.lora.api.LoraMessage;

public class Rak7249FluxLoraTransformer extends AbstractTransformer {
	public static Logger log = LoggerFactory.getLogger(Rak7249FluxLoraTransformer.class);
	private Rak7249Service service;

	public Rak7249FluxLoraTransformer(Rak7249Service service) {
		this.service = service;
	}

	@Override
	protected Object doTransform(Message<?> message) {
		log.info("doTransform procssing message=" + message);

		var payload = message.getPayload();

		Message< ? extends LoraMessage> loraMessage;

		if(payload instanceof Rak7249MessageJoin)
			loraMessage = service.convertJoinToLoraMessage((Message<Rak7249MessageJoin>)message);

		else if(payload instanceof Rak7249MessageAck)
			loraMessage = service.convertAckToLoraMessage((Message<Rak7249MessageAck>)message);

		else if(payload instanceof Rak7249MessageRx)
			loraMessage = service.convertRxToLoraMessage((Message<Rak7249MessageRx>)message);
		else
			throw new RuntimeException("Unknown message type: " + message.getPayload().getClass().getName());

		log.info("doTransform loraMessage = " + loraMessage);
		return loraMessage;
	}
}
