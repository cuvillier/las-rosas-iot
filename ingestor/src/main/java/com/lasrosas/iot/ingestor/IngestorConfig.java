package com.lasrosas.iot.ingestor;

import java.util.List;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.validation.annotation.Validated;

import com.lasrosas.iot.database.entities.tsr.TimeSeriePoint;
import com.lasrosas.iot.ingestor.parser.PayloadParsers;
import com.lasrosas.iot.ingestor.parser.impl.adenuis.AdenuisARF8180BAParser;
import com.lasrosas.iot.ingestor.parser.impl.elsys.ElsysErsParser;
import com.lasrosas.iot.ingestor.parser.impl.elsys.ElsysGenericParser;
import com.lasrosas.iot.ingestor.parser.impl.elsys.ElsysMB7389Parser;
import com.lasrosas.iot.mqtt.MqttSession;
import com.lasrosas.iot.shared.utils.LocalTopic;

@ConfigurationProperties
@Validated
public class IngestorConfig {

	@Bean
	public LoraIngestor LoraIngestor(LoraServerRAK7249 rak7249, PayloadParsers sensors) {
		return new LoraIngestor(rak7249, sensors);
	}

	@Bean
	public LoraServerRAK7249 loraServerRAK7249(@Qualifier("rak7249.mqtt") MqttSession mqtt) {
		return new LoraServerRAK7249(mqtt);
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

	@Bean("rak7249.mqtt")
	@ConfigurationProperties(prefix = "lora-servers.rak7249.mqtt")
	public MqttSession mqttRak7249() {
		return new MqttSession();
	}

	@Bean
	public SendMessageToTwin sendMessageToTwin(@Qualifier("mqtt") MqttSession mqtt) {
		return new SendMessageToTwin(mqtt);
	}

	@Bean(name="newPointTopic")
	public LocalTopic<List<TimeSeriePoint>> newPointTopic() {
		return new LocalTopic<List<TimeSeriePoint>>();
	}
}
