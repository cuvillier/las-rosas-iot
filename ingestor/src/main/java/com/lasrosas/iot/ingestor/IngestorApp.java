package com.lasrosas.iot.ingestor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

import com.lasrosas.iot.database.IOTDatabaseConfig;
import com.lasrosas.iot.influxdb.InfluxdbSession;

@SpringBootApplication(scanBasePackages="com.lasrosas.iot")
@Import({IOTDatabaseConfig.class, IngestorConfig.class})
public class IngestorApp implements CommandLineRunner {

	@Autowired
	private InfluxdbSession writeMessageToInfluxDB;

	@Autowired
	private SendMessageToTwin sendMessageToTwin;

	@Autowired
	private LoraIngestor loraIngestor;

	@Autowired
	private LoraServerRAK7249 rak7249;
	
	private long txid = System.currentTimeMillis();

	@Override
	public void run(String... args) throws Exception {

		this.rak7249.start(c -> {
			var newPoints = loraIngestor.handleLoraMessage(rak7249, c);
			writeMessageToInfluxDB.write(newPoints);
			sendMessageToTwin.send(newPoints, txid);
		});

		while(true) {
			Thread.sleep(60*60*1000);
		}
	}

	public static final void main(String... args) {
		SpringApplication.run(IngestorApp.class, args);
	}
}
