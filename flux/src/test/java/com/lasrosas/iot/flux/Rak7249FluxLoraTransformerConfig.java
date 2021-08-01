package com.lasrosas.iot.flux;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.integration.annotation.MessagingGateway;
import org.springframework.integration.annotation.Transformer;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.channel.QueueChannel;
import org.springframework.integration.dsl.Pollers;
import org.springframework.integration.scheduling.PollerMetadata;
import org.springframework.messaging.PollableChannel;

import com.lasrosas.iot.ingestor.services.rak7249.api.Rak7249FluxLoraTransformer;
import com.lasrosas.iot.ingestor.services.rak7249.api.Rak7249Service;
import com.lasrosas.iot.ingestor.services.rak7249.impl.Rak7249ServiceImpl;

public class Rak7249FluxLoraTransformerConfig {
	public static Logger log = LoggerFactory.getLogger(Rak7249FluxLoraTransformerConfig.class);

	@MessagingGateway(defaultRequestChannel = "inputChannel")
    public interface InputChannelGateway {
        void send(String data);
    }

    @Bean(name = PollerMetadata.DEFAULT_POLLER)
    public PollerMetadata poller() {                               // 11
    	return Pollers.fixedDelay(1000).get();
    }

	@Bean
	public DirectChannel inputChannel() {
		return new DirectChannel();
	}

	@Bean
	public PollableChannel outputChannel() {
		return new QueueChannel();
	}
	
	@Bean
	public Rak7249Service rak7249MessagesService() {
		return new Rak7249ServiceImpl();
	}

	@Bean
	@Transformer(inputChannel = "inputChannel", outputChannel = "outputChannel")
	public Rak7249FluxLoraTransformer rak7249FluxLoraTransformer(Rak7249Service service) {
		log.info("Ceate Rak7249FluxLoraTransformer");
		return new Rak7249FluxLoraTransformer(service);
	}
	
}
