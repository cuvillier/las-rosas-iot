package com.lasrosas.iot.ingestor;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;

import com.lasrosas.iot.database.IOTDatabaseConfig;
import com.lasrosas.iot.database.entities.tsr.TimeSeriePoint;
import com.lasrosas.iot.shared.utils.LocalTopic;

@SpringBootApplication(scanBasePackages="com.lasrosas.iot")
@PropertySource({
		"file:${config.path}/LasRosasIOT.properties",
		"file:${config.path}/IngestorApp.properties",
		"classpath:Database-jar.properties",
		"classpath:IngestorApp-jar.properties"
})
@Import(IOTDatabaseConfig.class)
public class IngestorApp {

	public static class Ingestor implements CommandLineRunner {

		@Autowired
		private LocalTopic<List<TimeSeriePoint>> newPointTopic;

		@Autowired
		private WriteMessageToInfluxDB writeMessageToInfluxDB;

		@Autowired
		private SendMessageToTwin sendMessageToTwin;


		private LoraIngestor loraIngestor;

		public Ingestor(LoraIngestor ingestor) {
			this.loraIngestor = ingestor;
		}

		@Override
		public void run(String... args) throws Exception {

			newPointTopic.subcribe(writeMessageToInfluxDB::send);
			newPointTopic.subcribe(sendMessageToTwin::send);

			loraIngestor.start(newPointTopic);
			
			while(true) {
				Thread.sleep(60*60*1000);
			}
		}
	}

	public static final void main(String... args) {
		SpringApplication.run(IngestorApp.class, args);
	}

	@Bean
	public CommandLineRunner commandLineRunner(ApplicationContext ctx) {
		var ingestor = ctx.getBean(LoraIngestor.class);
		return new Ingestor(ingestor);
	}
}
