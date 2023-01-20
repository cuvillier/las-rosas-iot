package com.lasrosas.iot.alarm;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.lasrosas.iot.alarm.database.repo.TelemetryWatcherRepo;
import com.lasrosas.iot.core.database.entities.dtw.BaseDatabaseTest;
import com.lasrosas.iot.core.database.repo.ThingRepo;
import com.lasrosas.iot.core.shared.telemetry.AirEnvironment;

public class TelemetryWatcherRepoTest extends BaseDatabaseTest {

	@Autowired
	private ThingRepo thingRepo;

	@Autowired
	private TelemetryWatcherRepo telemetryWatcherRepo;

	@PersistenceContext
	private EntityManager em;

	@Test
	public void test() {
		var payload = new AirEnvironment(10.0, 20.0, 4.0);
		var thing = thingRepo.getReferenceById(6L);
		var result = telemetryWatcherRepo.findMatchingWatchers(thing, payload);

		System.out.println(result.size());
	}
}
