package com.lasrosas.iot.core.database;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;

import com.google.gson.Gson;
import com.lasrosas.iot.core.database.repo.ThingRepo;
import com.lasrosas.iot.core.shared.utils.UtilsConfig;

@SpringBootApplication
@EnableTransactionManagement
@Import({UtilsConfig.class, IOTDatabaseConfig.class})
public class DatabaseCLI {

	public static final Logger log = LoggerFactory.getLogger(DatabaseCLI.class);

	public static final void main(String... args) {
		var context = SpringApplication.run(DatabaseCLI.class, args);
		var bean = context.getBean(DatabaseCLI.class);
		bean.exportThing("thing.json");
	}

	@Autowired
	private ThingRepo thgRepo;

	@Autowired
	private Gson gson;

	@Transactional
	public void exportThing(String file) {
		for(var thg: thgRepo.findAll().toArray()) {
			
		}
	}

	public void importThing(String file, boolean replace) {
		
	}
}
