package com.lasrosas.iot.core.ingestor.parsers.impl;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.validation.annotation.Validated;

import com.lasrosas.iot.core.ingestor.parsers.impl.adeunis.AdeunisARF8170BAParser;
import com.lasrosas.iot.core.ingestor.parsers.impl.adeunis.AdeunisARF8180BAParser;
import com.lasrosas.iot.core.ingestor.parsers.impl.dragino.DraginoLHT65Parser;
import com.lasrosas.iot.core.ingestor.parsers.impl.elsys.ElsysErsParser;
import com.lasrosas.iot.core.ingestor.parsers.impl.elsys.ElsysGenericParser;
import com.lasrosas.iot.core.ingestor.parsers.impl.elsys.ElsysMB7389Parser;
import com.lasrosas.iot.core.ingestor.parsers.impl.mfc88.MFC88LW13IOParser;

@ConfigurationProperties
@Validated
public class SensorsConfig {

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
	public ElsysMB7389Parser ElsysMB7389Parser(ElsysGenericParser ElsysGenericParser) {
		return new ElsysMB7389Parser(ElsysGenericParser);
	}

	@Bean
	public MFC88LW13IOParser MFC88LW13IOParser() {
		return new MFC88LW13IOParser();
	}

	@Bean
	public DraginoLHT65Parser DraginoLHT65Parser() {
		return new DraginoLHT65Parser();
	}

	@Bean
	@ConfigurationProperties(prefix = "lora-sensors")
	public SensorServiceImpl LoraSensors(
			AdeunisARF8180BAParser adenuisARF8180BAParser,
			AdeunisARF8170BAParser adenuisARF8170BAParser,
			ElsysErsParser elsysErsParser,
			ElsysMB7389Parser elsysMB7389Parser,
			MFC88LW13IOParser mfc88LW1310Parser,
			DraginoLHT65Parser draginoLHT65Parser) {

		return new SensorServiceImpl(adenuisARF8180BAParser, adenuisARF8170BAParser, elsysErsParser, elsysMB7389Parser, mfc88LW1310Parser, draginoLHT65Parser);
	}
}
