package com.lasrosas.iot.reactor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;
import org.springframework.integration.config.EnableIntegration;

import com.lasrosas.iot.database.IOTDatabaseConfig;

@SpringBootApplication(scanBasePackages="com.lasrosas.iot")
@Import({IOTDatabaseConfig.class, ReactorConfig.class})
public class ReactorApp implements CommandLineRunner {
	public static final Logger log = LoggerFactory.getLogger(ReactorApp.class);

	private ReactorEngine reactor;

	public ReactorApp(ReactorEngine reactor) {
		this.reactor = reactor;
	}

	public static final void main(String... args) {
		SpringApplication.run(ReactorApp.class, args);
		log.warn("EXIT Main");
	}

	@Override
	public void run(String... args) throws Exception {
		log.info("Starting");
		reactor.start();
		log.info("Started");
	}

}
