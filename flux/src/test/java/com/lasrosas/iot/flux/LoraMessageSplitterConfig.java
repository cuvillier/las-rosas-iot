package com.lasrosas.iot.flux;

import org.springframework.context.annotation.Bean;
import org.springframework.integration.annotation.Router;
import org.springframework.integration.annotation.Splitter;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.channel.QueueChannel;
import org.springframework.integration.router.MessageRouter;
import org.springframework.integration.router.PayloadTypeRouter;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.PollableChannel;

import com.lasrosas.iot.ingestor.services.lora.api.LoraMessageSplitter;
import com.lasrosas.iot.ingestor.services.lora.api.LoraMetricMessage;
import com.lasrosas.iot.ingestor.services.lora.api.LoraService;
import com.lasrosas.iot.ingestor.services.lora.impl.LoraServiceImpl;
import com.lasrosas.iot.ingestor.services.sensors.api.ThingEncodedMessage;

public class LoraMessageSplitterConfig {
	@Bean
	public DirectChannel inputChannel() {
		return new DirectChannel();
	}

	@Bean
	public DirectChannel mixedChannel() {
		return new DirectChannel();
	}

	@Bean
	public PollableChannel loraMetricChannel() {
		return new QueueChannel();
	}

	@Bean
	public PollableChannel thingEncodedChannel() {
		return new QueueChannel();
	}

	@Bean
	public LoraService loraService() {
		return new LoraServiceImpl();
	}


	@Bean
	@Splitter(inputChannel = "inputChannel")
	public LoraMessageSplitter splitLoraMessage(LoraService service, MessageChannel mixedChannel) {
		var splitter = new LoraMessageSplitter(service);
		splitter.setOutputChannel(mixedChannel);
		return splitter;
	}

	@Bean
	@Router(inputChannel = "mixedChannel")
	public MessageRouter routeLoraMessage(LoraService service) {
		var router = new PayloadTypeRouter();
		router.setChannelMapping(LoraMetricMessage.class.getName(), "loraMetricChannel");
		router.setChannelMapping(ThingEncodedMessage.class.getName(), "thingEncodedChannel");
		return router;
	}

}
