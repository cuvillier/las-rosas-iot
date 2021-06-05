package com.lasrosas.iot.ingestor;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.validation.annotation.Validated;

import com.lasrosas.iot.ingestor.parser.PayloadParsers;
import com.lasrosas.iot.ingestor.parser.impl.adeunis.AdeunisARF8170BAParser;
import com.lasrosas.iot.ingestor.parser.impl.adeunis.AdeunisARF8180BAParser;
import com.lasrosas.iot.ingestor.parser.impl.elsys.ElsysErsParser;
import com.lasrosas.iot.ingestor.parser.impl.elsys.ElsysGenericParser;
import com.lasrosas.iot.ingestor.parser.impl.elsys.ElsysMB7389Parser;
import com.lasrosas.iot.ingestor.parser.mfc88.MFC88LW13IOParser;
import com.lasrosas.iot.mqtt.session.MqttSession;

@ConfigurationProperties
@Validated
public class IngestorConfig {

	@Bean
	public LoraIngestor LoraIngestor(PayloadParsers sensors) {
		return new LoraIngestor(sensors);
	}

	@Bean
	public LoraServerRAK7249 loraServerRAK7249(@Qualifier("rak7249.mqtt") MqttSession mqtt) {
		return new LoraServerRAK7249(mqtt);
	}

	@Bean
	public AdeunisARF8180BAParser AdenuisARF8180BAParser() {
		return new AdeunisARF8180BAParser();
	}

	@Bean
	public AdeunisARF8170BAParser AdenuisARF8170BAParser() {
		return new AdeunisARF8170BAParser();
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
	public MFC88LW13IOParser MFC88LW13IOParser() {
		return new MFC88LW13IOParser();
	}

	@Bean
	@ConfigurationProperties(prefix = "lora-sensors")
	public PayloadParsers LoraSensors(
			AdeunisARF8180BAParser adenuisARF8180BAParser,
			AdeunisARF8170BAParser adenuisARF8170BAParser,
			ElsysErsParser elsysErsParser,
			ElsysMB7389Parser elsysMB7389Parser,
			MFC88LW13IOParser mfc88LW1310Parser) {
		
		return new PayloadParsers(adenuisARF8180BAParser, adenuisARF8170BAParser, elsysErsParser, elsysMB7389Parser, mfc88LW1310Parser);
	}

	@Bean("rak7249.mqtt")
	@ConfigurationProperties(prefix = "lora-servers.rak7249.mqtt")
	public MqttSession mqttRak7249() {
		return new MqttSession();
	}

	@Bean
	public SendMessageToTwin sendMessageToTwin(@Qualifier("out.mqtt") MqttSession mqtt) {
		return new SendMessageToTwin(mqtt);
	}

	@Bean("out.mqtt")
	@ConfigurationProperties(prefix = "out.mqtt")
	public MqttSession MqttSession() {
		return new MqttSession();
	}
}
