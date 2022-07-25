package com.lasrosas.iot.core.ingestor.timeSerieWriter.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.HashMap;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.lasrosas.iot.core.ingestor.timeSerieWriter.api.InfluxDBConfig;

public class WriteInfluxDBImplTest {

	private WriteInfluxDBImpl cut;

	@BeforeEach
	public void init() {
		var config = new InfluxDBConfig("http://localhost:8086", "73zfqUif-mWq90-4QtqMIYuHt73xk8wRfavbkXIwjLFe8hXFvgwHGE2HyVWSMupXtpyybBhwMeSB9d9lKzhxTA==", "las-rosas.es", "lasrosasiotUnitTest");
		cut = new WriteInfluxDBImpl(config);
	}

	@Test
	public void addFields() {
		var telemetry = new TelemetryTest();

		var fields = new HashMap<String, Object>();
		cut.addFields(fields, telemetry, null);

		for(var entry: fields.entrySet()) {
			System.out.println(entry.getKey() + " = " + entry.getValue());
		}

		assertEquals((int)fields.get("valuesHolder_glop"), telemetry.getValuesHolder().getGlop());
		assertEquals((String)fields.get("stringValue"), telemetry.getStringValue());
		assertEquals((int)fields.get("intValue"), telemetry.getIntValue());
		assertEquals((float)fields.get("floatValue"), telemetry.getFloatValue(), 0.0001);
		assertEquals((short)fields.get("shortValue"), telemetry.getShortValue());
		assertEquals((boolean)fields.get("booleanValue"), telemetry.isBooleanValue());
		assertEquals((double)fields.get("doubleValue"), telemetry.getDoubleValue(), 0.0001);
		assertEquals((long)fields.get("longValue"), telemetry.getLongValue());
	}

/*
	@Test
	public void writeToInfluxDB() {
		var telemetry = new TelemetryTest();
		cut.writePoint("TelemetryTest", telemetry);
	}
*/
}
