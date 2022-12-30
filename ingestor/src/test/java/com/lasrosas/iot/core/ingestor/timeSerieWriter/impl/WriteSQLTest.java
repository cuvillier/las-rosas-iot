package com.lasrosas.iot.core.ingestor.timeSerieWriter.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.lasrosas.iot.core.database.entities.dtw.BaseDatabaseTest;
import com.lasrosas.iot.core.database.entities.thg.Thing;
import com.lasrosas.iot.core.database.entities.thg.ThingGateway;
import com.lasrosas.iot.core.database.entities.thg.ThingLora;
import com.lasrosas.iot.core.database.entities.thg.ThingType;
import com.lasrosas.iot.core.database.entities.tsr.TimeSerie;
import com.lasrosas.iot.core.database.entities.tsr.TimeSeriePoint;
import com.lasrosas.iot.core.database.entities.tsr.TimeSerieType;
import com.lasrosas.iot.core.ingestor.timeSerieWriter.api.WriteSQL;

@ContextConfiguration(classes = {WriteSQLConfig.class})
public class WriteSQLTest extends BaseDatabaseTest {
	public static final Logger log = LoggerFactory.getLogger(WriteSQLTest.class);

	@Autowired
	private Gson gson;

	@Autowired
	private WriteSQL cut;

	@Test
	public void testUpdateProxy() {
		assertNotNull(gson);

		var tht = new ThingType("LasRosas", "UnitTest");
		var gtw = new ThingGateway("unittest");
		var thg = new ThingLora(gtw, tht, "123456");
		thg.createProxy();

		var point = createPoint(thg, "{ i: 1 }", "UnitTest", null);

		cut.setStoreProxyTime(true);
		cut.updateProxy(point);

		var proxy = thg.getProxy();
		assertNotNull(proxy);

		log.debug(proxy.getValues());

		var value = gson.fromJson(proxy.getValues(), JsonObject.class);
		var subvalue = value.getAsJsonObject("UnitTest");
		assertEquals( 2, subvalue.entrySet().size());
		assertEquals( 1, subvalue.get("i").getAsInt());
		assertNotNull(subvalue.get("i-time").getAsString());

		// Update the value of i
		var point2 = new TimeSeriePoint(point.getTimeSerie(), LocalDateTime.now(), "{i: 2}");
		cut.updateProxy(point2);

		value = gson.fromJson(proxy.getValues(), JsonObject.class);
		subvalue = value.getAsJsonObject("UnitTest");
		assertEquals( 2, subvalue.entrySet().size());
		assertEquals( 2, subvalue.get("i").getAsInt());
		assertNotNull(subvalue.get("i-time").getAsString());

		// Add new value
		var point3 = new TimeSeriePoint(point.getTimeSerie(), LocalDateTime.now(), "{j: 314}");
		cut.updateProxy(point3);

		value = gson.fromJson(proxy.getValues(), JsonObject.class);
		subvalue = value.getAsJsonObject("UnitTest");
		assertEquals( 4, subvalue.entrySet().size());
		assertEquals( 2, subvalue.get("i").getAsInt());
		assertNotNull(subvalue.get("i-time").getAsString());
		assertEquals( 314, subvalue.get("j").getAsInt());
		assertNotNull(subvalue.get("j-time").getAsString());

		var point4 = createPoint(thg, "{ k: 1789 }", "UnitTest2", null);
		cut.updateProxy(point4);

		value = gson.fromJson(proxy.getValues(), JsonObject.class);
		assertEquals( 2, value.entrySet().size());
		assertNotNull(value.get("UnitTest"));
		assertNotNull(value.get("UnitTest2"));

		subvalue = value.getAsJsonObject("UnitTest2");
		assertEquals( 2, subvalue.entrySet().size());
		assertEquals( 1789, subvalue.get("k").getAsInt());
		assertNotNull(subvalue.get("k-time").getAsString());
	}

	private TimeSeriePoint createPoint(Thing thg, String value, String schema, String sensor) {
		var tst = new TimeSerieType(schema);
		var tsr = new TimeSerie(thg, tst, sensor);
		
		return new TimeSeriePoint(tsr, LocalDateTime.now(), value);
	}
}
