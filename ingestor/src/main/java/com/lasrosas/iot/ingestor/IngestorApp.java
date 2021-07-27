package com.lasrosas.iot.ingestor;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

import com.lasrosas.iot.database.IOTDatabaseConfig;

@SpringBootApplication(scanBasePackages="com.lasrosas.iot")
@Import({IOTDatabaseConfig.class, IngestorConfig.class})
public class IngestorApp {

	public static final void main(String... args) {
		SpringApplication.run(IngestorApp.class, args);
	}
}
