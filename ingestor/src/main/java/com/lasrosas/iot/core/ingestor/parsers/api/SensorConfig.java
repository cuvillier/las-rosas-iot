package com.lasrosas.iot.core.ingestor.parsers.api;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;

import com.lasrosas.iot.core.ingestor.parsers.impl.SensorServiceImpl;
import com.lasrosas.iot.core.ingestor.parsers.impl.adeunis.AdeunisARF8170BAParser;
import com.lasrosas.iot.core.ingestor.parsers.impl.adeunis.AdeunisARF8180BAParser;
import com.lasrosas.iot.core.ingestor.parsers.impl.dragino.DraginoLHT65Parser;
import com.lasrosas.iot.core.ingestor.parsers.impl.elsys.ElsysErsParser;
import com.lasrosas.iot.core.ingestor.parsers.impl.elsys.ElsysGenericParser;
import com.lasrosas.iot.core.ingestor.parsers.impl.elsys.ElsysMB7389Parser;
import com.lasrosas.iot.core.ingestor.parsers.impl.mfc88.MFC88LW13IOParser;

@ConfigurationProperties
public class SensorConfig {

	@Bean
	public AdeunisARF8170BAParser AdeunisARF8170BAParser() {
		return new AdeunisARF8170BAParser();
	}

	@Bean
	public AdeunisARF8180BAParser AdeunisARF8180BAParser() {
		return new AdeunisARF8180BAParser();
	}

	@Bean
	public ElsysErsParser ElsysErsParser() {
		return new ElsysErsParser();
	}

	@Bean
	public ElsysMB7389Parser ElsysMB7389Parser() {
		return new ElsysMB7389Parser(new ElsysGenericParser());
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
	public SensorService sensorService(
			AdeunisARF8170BAParser AdeunisARF8170BAParser,
			AdeunisARF8180BAParser AdeunisARF8180BAParser,
			ElsysErsParser ElsysErsParser,
			ElsysMB7389Parser ElsysMB7389Parser,
			MFC88LW13IOParser MFC88LW13IOParser,
			DraginoLHT65Parser DraginoLHT65Parser
			) {
		return new SensorServiceImpl(
			AdeunisARF8170BAParser,
			AdeunisARF8180BAParser,
			ElsysErsParser,
			ElsysMB7389Parser,
			MFC88LW13IOParser,
			DraginoLHT65Parser
		);
	}
}
