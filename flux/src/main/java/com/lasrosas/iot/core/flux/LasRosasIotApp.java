package com.lasrosas.iot.core.flux;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;
import org.springframework.integration.channel.QueueChannel;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.google.gson.Gson;
import com.lasrosas.iot.core.database.IOTDatabaseConfig;
import com.lasrosas.iot.core.ingestor.parsers.api.SensorConfig;
import com.lasrosas.iot.core.reactor.reactores.ReactorConfig;

@SpringBootApplication
@EnableTransactionManagement
@Import({IOTDatabaseConfig.class, SensorConfig.class, ReactorConfig.class, LasRosasIotConfig.class, LasRosasIotBaseConfig.class})
public class LasRosasIotApp {
	public static final Logger log = LoggerFactory.getLogger(LasRosasIotApp.class);

	public static final void main(String... args) {
		var context = SpringApplication.run(LasRosasIotApp.class, args);
		var errorChannel = context.getBean(QueueChannel.class);

		var gson = context.getBean(Gson.class);
		while(true) {
			var message = errorChannel.receive();
			log.error("Error channel say: ", gson.toJson(message));
		}
	}
}
