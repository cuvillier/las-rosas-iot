package com.lasrosas.iot.flux;

import org.springframework.context.annotation.Bean;
import org.springframework.integration.annotation.Transformer;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.channel.QueueChannel;
import org.springframework.messaging.PollableChannel;

import com.lasrosas.iot.ingestor.services.sensors.api.DecodeThingMessageTransformer;
import com.lasrosas.iot.ingestor.services.sensors.api.SensorService;
import com.lasrosas.iot.ingestor.services.sensors.impl.SensorServiceImpl;

public class DecodeThingMessageTransformerConfig {
	@Bean
	public DirectChannel inputChannel() {
		return new DirectChannel();
	}

	@Bean
	public PollableChannel outputChannel() {
		return new QueueChannel();
	}

	@Bean
	@Transformer(inputChannel = "inputChannel", outputChannel = "outputChannel")
	public DecodeThingMessageTransformer splitLoraMessage(SensorService sensorService) {
		return new DecodeThingMessageTransformer(sensorService);
	}
}
