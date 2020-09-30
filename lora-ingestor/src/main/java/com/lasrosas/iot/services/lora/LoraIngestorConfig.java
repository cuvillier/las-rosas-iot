package com.lasrosas.iot.services.lora;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.lasrosas.iot.services.db.entities.tsr.TimeSeriePoint;
import com.lasrosas.iot.services.lora.parser.PayloadParsers;
import com.lasrosas.iot.services.lora.parser.impl.adenuis.AdenuisARF8180BAParser;
import com.lasrosas.iot.services.lora.parser.impl.elsys.ElsysErsParser;
import com.lasrosas.iot.services.lora.parser.impl.elsys.ElsysGenericParser;
import com.lasrosas.iot.services.lora.parser.impl.elsys.ElsysMB7389Parser;
import com.lasrosas.iot.services.utils.LocalTopic;


@Configuration
public class LoraIngestorConfig {
	
	@Bean
	@ConfigurationProperties(prefix = "lora-ingestor")
	public LoraIngestor LoraIngestor(LoraServerRAK7249Mqtt rak7249mqtt, PayloadParsers sensors, InfluxdbWriter influxdbWriter) {
		return new LoraIngestor(rak7249mqtt, sensors, influxdbWriter);
	}

	@Bean
	public LoraServerRAK7249 LoraServerRAK7249() {
		return new LoraServerRAK7249();
	}

	@Bean
	public AdenuisARF8180BAParser AdenuisARF8180BAParser() {
		return new AdenuisARF8180BAParser();
	}

	@Bean
	public ElsysGenericParser ElsysGenericParser() {
		return new ElsysGenericParser();
	}

	@Bean
	public ElsysErsParser ElsysErsParser() {
		return new ElsysErsParser();
	}

	@Bean
	public ElsysMB7389Parser ElsysMB7389Parser() {
		return new ElsysMB7389Parser();
	}

	@Bean
	@ConfigurationProperties(prefix = "lora-sensors")
	public PayloadParsers LoraSensors(
			AdenuisARF8180BAParser adenuisARF8180BAParser,
			ElsysErsParser elsysErsParser,
			ElsysMB7389Parser elsysMB7389Parser) {
		
		return new PayloadParsers(adenuisARF8180BAParser, elsysErsParser, elsysMB7389Parser);
	}

	@Bean
	@ConfigurationProperties(prefix = "rak7249")
	public LoraServer loraServerRAK4972() {
		return new LoraServerRAK7249();
	}

	@Bean
	@ConfigurationProperties(prefix = "rak7249")
	public LoraServerRAK7249Mqtt loraServerRAK4972Mqtt(LoraServerRAK7249 loraServerRAK7249) {
		return new LoraServerRAK7249Mqtt(loraServerRAK7249);
	}

	@Bean
	public InfluxdbWriter InfluxdbWriter() {
		return new InfluxdbWriter();
	}

	@Bean(name="newPointTopic")
	public LocalTopic<TimeSeriePoint> newPointTopic() {
		return new LocalTopic<TimeSeriePoint>();
	}
}
