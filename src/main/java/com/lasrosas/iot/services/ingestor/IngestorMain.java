package com.lasrosas.iot.services.ingestor;

import org.springframework.boot.SpringApplication;

public class IngestorMain {

	public static final void main(String ... args) {
		try {
			SpringApplication app = new SpringApplication(IngestorConfig.class);
			app.run(args);
		} catch(Exception e) {
			throw new RuntimeException(e);
		}
	}
}
