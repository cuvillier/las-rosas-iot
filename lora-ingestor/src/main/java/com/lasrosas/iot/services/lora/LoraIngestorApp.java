package com.lasrosas.iot.services.lora;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;

import com.lasrosas.iot.services.db.IOTDatabaseConfig;

@SpringBootApplication(scanBasePackages="com.lasrosas.iot")
@PropertySource("classpath:LoraIngestorApp.properties")
@Import(IOTDatabaseConfig.class)
public class LoraIngestorApp {

	public static class LoraIngestorLineRunner implements CommandLineRunner {
		private LoraIngestor ingestor;

		public LoraIngestorLineRunner(LoraIngestor ingestor) {
			this.ingestor = ingestor;
		}

		@Override
		public void run(String... args) throws Exception {
			ingestor.start();
		}
	}

	public static final void main(String... args) {
		SpringApplication.run(LoraIngestorApp.class, args);
	}

	@Bean
	public CommandLineRunner commandLineRunner(ApplicationContext ctx) {
		var ingestor = ctx.getBean(LoraIngestor.class);
		return new LoraIngestorLineRunner(ingestor);
	}
}
