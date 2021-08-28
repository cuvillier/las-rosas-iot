package com.lasrosas.iot.core.ingestor.sensors.api;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;

import com.lasrosas.iot.core.ingestor.sensors.impl.SensorServiceImpl;
import com.lasrosas.iot.core.ingestor.sensors.impl.adeunis.AdeunisARF8170BAParser;
import com.lasrosas.iot.core.ingestor.sensors.impl.adeunis.AdeunisARF8180BAParser;
import com.lasrosas.iot.core.ingestor.sensors.impl.elsys.ElsysErsParser;
import com.lasrosas.iot.core.ingestor.sensors.impl.elsys.ElsysMB7389Parser;
import com.lasrosas.iot.core.ingestor.sensors.impl.mfc88.MFC88LW13IOParser;

@ConfigurationProperties
public class SensorConfig {
	@Bean
	public SensorService sensorService() {
		return new SensorServiceImpl(
			new AdeunisARF8170BAParser(),
			new AdeunisARF8180BAParser(),
			new ElsysErsParser(),
			new ElsysMB7389Parser(),
			new MFC88LW13IOParser()
		);
	}
}
