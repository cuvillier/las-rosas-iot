package com.lasrosas.iot.core.flux;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.annotation.IntegrationComponentScan;
import org.springframework.integration.channel.PublishSubscribeChannel;
import org.springframework.integration.config.EnableIntegration;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.transaction.annotation.Transactional;

import com.google.gson.Gson;
import com.lasrosas.iot.core.database.entities.SampleData;
import com.lasrosas.iot.core.database.entities.dtw.BaseDatabaseTest;
import com.lasrosas.iot.core.database.repo.ThingLoraRepo;
import com.lasrosas.iot.core.flux.WaterTankTestConfig.TelemetryGateway;
import com.lasrosas.iot.core.shared.telemetry.AirEnvironment;
import com.lasrosas.iot.core.shared.telemetry.DistanceMeasurement;
import com.lasrosas.iot.core.shared.telemetry.Telemetry;
import com.lasrosas.iot.core.shared.telemetry.WaterTankFilling;
import com.lasrosas.iot.core.shared.utils.LasRosasHeaders;

@EnableIntegration
@ContextConfiguration(classes = { WaterTankTestConfig.class})
@IntegrationComponentScan("com.lasrosas.iot.core")
public class WaterTankTest extends BaseDatabaseTest {
	public static Logger log = LoggerFactory.getLogger(WaterTankTest.class);

	@Autowired
	private ThingLoraRepo thingRepo;

	@Autowired
	private TelemetryGateway telemetryGateway;

	@Autowired
	private PublishSubscribeChannel telemetryChannel;

	@Autowired
	private Gson gson;

	@Test
	@DirtiesContext
	@Transactional
	public void test_WanterTank() {

		var distanceSensor = thingRepo.getByDeveui(SampleData.WATER_TANK_DISTANCE_SENSOR_DEVEUI).get();

		final var expectedValues = new WaterTankFilling[] {
				new WaterTankFilling(3.14, 100.0),
				new WaterTankFilling(3.14, 100.0),
				new WaterTankFilling(3.08, 98.13),
				new WaterTankFilling(2.97, 94.79),
				new WaterTankFilling(2.84, 90.59),
				new WaterTankFilling(2.69, 85.76),
				new WaterTankFilling(2.52, 80.44),
				new WaterTankFilling(2.34, 74.76),
				new WaterTankFilling(2.16, 68.8),
				new WaterTankFilling(1.96, 62.64),
				new WaterTankFilling(1.77, 56.35)
		};

		final var i = new int[] {0};

		telemetryChannel.subscribe((m) -> {
			if(m.getPayload() instanceof DistanceMeasurement) return;

			var expected = expectedValues[i[0]++];
			var received = (WaterTankFilling)m.getPayload();
			assertEquals(expected.getVolume(), received.getVolume());
			assertEquals(expected.getPercentage(), received.getPercentage());
		});

		for(var d = 0.0; d < 1.0; d += 0.1) {
			var distance = new DistanceMeasurement(d);
			var imessage = MessageBuilder.withPayload((Telemetry)distance)
					.setHeader(LasRosasHeaders.THING_ID, distanceSensor.getTechid()).build();
			telemetryGateway.sendTelemetry(imessage);
		}

		assertEquals(i[0], expectedValues.length);
	}
}

