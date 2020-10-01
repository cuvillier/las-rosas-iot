package com.lasrosas.iot.reactor;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;

import com.lasrosas.iot.database.IOTDatabaseConfig;

@SpringBootApplication(scanBasePackages="com.lasrosas.iot")
@PropertySource("classpath:ReactorApp.properties")
@Import(IOTDatabaseConfig.class)
public class ReactorApp {

	public static class ReactorLineRunner implements CommandLineRunner {
		private Reactor reactor;

		public ReactorLineRunner(Reactor reactor) {
			this.reactor = reactor;
		}

		@Override
		public void run(String... args) throws Exception {
			reactor.start();
		}
	}

	public static final void main(String... args) {
		SpringApplication.run(ReactorApp.class, args);
	}

	@Bean
	public CommandLineRunner commandLineRunner(ApplicationContext ctx) {
		var reactor = ctx.getBean(Reactor.class);
		return new ReactorLineRunner(reactor);
	}
}
