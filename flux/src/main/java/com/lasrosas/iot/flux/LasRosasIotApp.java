package com.lasrosas.iot.flux;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

import com.lasrosas.iot.database.IOTDatabaseConfig;

@SpringBootApplication(scanBasePackages="com.lasrosas.iot")
@Import({IOTDatabaseConfig.class, LasRosasIotConfig.class})
public class LasRosasIotApp {

	public static final void main(String... args) {
		SpringApplication.run(LasRosasIotApp.class, args);
	}
}
