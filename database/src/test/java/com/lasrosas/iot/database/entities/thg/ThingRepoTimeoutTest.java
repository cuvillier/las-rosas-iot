package com.lasrosas.iot.database.entities.thg;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.lasrosas.iot.core.database.repo.ThingRepo;
import com.lasrosas.iot.database.entities.dtw.BaseDatabaseTest;
import com.lasrosas.iot.database.entities.dtw.DatabaseTest;

public class ThingRepoTimeoutTest extends BaseDatabaseTest {
	private static final Logger log = LoggerFactory.getLogger(DatabaseTest.class);

	@Autowired
	private ThingRepo thingRepo;

	@Test
	@Transactional
	void findTesthing() throws Exception {
		// Just run it
		thingRepo.findTimeouted();
	}
}
