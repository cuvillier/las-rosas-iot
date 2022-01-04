package com.lasrosas.iot.database.entities.thg;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.lasrosas.iot.core.database.repo.ThingRepo;
import com.lasrosas.iot.database.entities.dtw.BaseDatabaseTest;
import com.lasrosas.iot.database.entities.dtw.DatabaseTest;

public class ThingRepoTimeoutTest extends BaseDatabaseTest {
	private static final Logger log = LoggerFactory.getLogger(DatabaseTest.class);

	@Autowired
	private ThingRepo thingRepo;

	private Gson gson = new GsonBuilder().setPrettyPrinting().create();

	@Test
	@Transactional
	void findTesthing() throws Exception {

		var result = thingRepo.findTimeouted();

		log.info("==================> " + gson.toJson(result));
	}
}
