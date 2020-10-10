package com.lasrosas.iot.reactor;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

import com.lasrosas.iot.database.IOTDatabaseConfig;

@SpringBootApplication(scanBasePackages="com.lasrosas.iot")
@Import({IOTDatabaseConfig.class, ReactorConfig.class})
public class ReactorApp implements CommandLineRunner {
	private Reactor reactor;

	public ReactorApp(Reactor reactor) {
		this.reactor = reactor;
	}

	public static final void main(String... args) {
		SpringApplication.run(ReactorApp.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		reactor.start();
	}

}
