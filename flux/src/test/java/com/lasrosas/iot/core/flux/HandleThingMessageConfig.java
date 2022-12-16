package com.lasrosas.iot.core.flux;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.messaging.MessageChannel;
import org.springframework.test.context.ContextConfiguration;

import com.lasrosas.iot.core.database.IOTDatabaseConfig;
import com.lasrosas.iot.core.ingestor.lora.api.LoraService;
import com.lasrosas.iot.core.ingestor.parsers.impl.SensorsConfig;

@ContextConfiguration(classes = { LasRosasIotBaseConfig.class, SensorsConfig.class, IOTDatabaseConfig.class})

public class HandleThingMessageConfig {

	@Autowired
	private LasRosasFluxDelegate delegate;

	@Bean
	public IntegrationFlow handleLoraMessages(MessageChannel loraChannel, LoraService service) {
		return delegate.handleLoraMessages(loraChannel, service);
	}
}
