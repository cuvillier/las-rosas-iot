package com.lasrosas.iot.core.flux;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;
import org.springframework.integration.channel.QueueChannel;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.google.gson.GsonBuilder;
import com.lasrosas.iot.core.database.IOTDatabaseConfig;
import com.lasrosas.iot.core.ingestor.parsers.api.SensorConfig;

@SpringBootApplication(scanBasePackages="com.lasrosas.iot.core")
@EnableTransactionManagement
@Import({IOTDatabaseConfig.class, SensorConfig.class, LasRosasIotConfig.class})
public class LasRosasIotApp {
	public static final Logger log = LoggerFactory.getLogger(LasRosasIotApp.class);

	public static final void main(String... args) {
		var context = SpringApplication.run(LasRosasIotApp.class, args);
		var errorChannel = context.getBean(QueueChannel.class);

		var gson = new GsonBuilder().setPrettyPrinting().create();
		while(true) {
			var message = errorChannel.receive();
			log.error("Error channel say: ", gson.toJson(message));
		}
	}
}
