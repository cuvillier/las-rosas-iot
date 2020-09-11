package com.lasrosas.iot.services.db.entities.dtw;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;
import org.springframework.test.annotation.Commit;

import com.lasrosas.iot.services.db.entities.thg.ThingGateway;
import com.lasrosas.iot.services.db.entities.thg.ThingLora;
import com.lasrosas.iot.services.db.entities.thg.ThingType;
import com.lasrosas.iot.services.db.entities.tsr.TimeSerie;
import com.lasrosas.iot.services.db.entities.tsr.TimeSeriePoint;
import com.lasrosas.iot.services.db.entities.tsr.TimeSerieType;

public class DatabaseTest extends BaseDatabaseTest {

	@Test
	@Commit
	void connectToTheDatabase() throws Exception {
		try {
			var q = em.createQuery("SELECT thg FROM Thing thg");
			q.getResultList();

			var gateway = new ThingGateway("Test gateway", "none");
			var thingType = new ThingType("Lora sample");
			var thing = new ThingLora(gateway, thingType, "1234567");
			thing.setDeveui("1234567890");

			em.persist(gateway);
			em.persist(thingType);
			em.persist(thing);

			var tsType = new TimeSerieType("TestTS");
			em.persist(tsType);

			var ts = new TimeSerie(thing, tsType);
			em.persist(ts);

			var time = LocalDateTime.now();
			for(int i = 0; i < 10; i++) {
				time = time.plusSeconds(1);
				var point = new TimeSeriePoint(ts, time, "{value=" + i + "}");
				em.persist(point);
			}

		} catch (Exception e) {
			throw e;
		}
	}
}
