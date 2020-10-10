package com.lasrosas.iot.ingestor;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Import;

import com.lasrosas.iot.database.IOTDatabaseConfig;
import com.lasrosas.iot.database.entities.tsr.TimeSeriePoint;
import com.lasrosas.iot.influxdb.InfluxdbSession;
import com.lasrosas.iot.shared.utils.LocalTopic;

@SpringBootApplication(scanBasePackages="com.lasrosas.iot")
@Import({IOTDatabaseConfig.class, IngestorConfig.class})
public class IngestorApp implements CommandLineRunner {
	
	@Autowired
	private LocalTopic<List<TimeSeriePoint>> newPointTopic;
	
	@Autowired
	private InfluxdbSession writeMessageToInfluxDB;
	
	@Autowired
	private SendMessageToTwin sendMessageToTwin;
		
	@Autowired
	private LoraIngestor loraIngestor;

	@Override
	public void run(String... args) throws Exception {

		newPointTopic.subcribe(writeMessageToInfluxDB::write);
		newPointTopic.subcribe(sendMessageToTwin::send);

		loraIngestor.start(newPointTopic);
		
		while(true) {
			Thread.sleep(60*60*1000);
		}
	}

	public static final void main(String... args) {
		SpringApplication.run(IngestorApp.class, args);
	}
}
