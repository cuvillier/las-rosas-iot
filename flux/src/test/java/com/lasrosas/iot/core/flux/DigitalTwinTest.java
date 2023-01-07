package com.lasrosas.iot.core.flux;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.annotation.IntegrationComponentScan;
import org.springframework.integration.channel.PublishSubscribeChannel;
import org.springframework.integration.config.EnableIntegration;
import org.springframework.messaging.PollableChannel;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.transaction.annotation.Transactional;

import com.google.gson.Gson;
import com.lasrosas.iot.core.database.entities.SampleData;
import com.lasrosas.iot.core.database.entities.dtw.BaseDatabaseTest;
import com.lasrosas.iot.core.database.repo.ThingLoraRepo;
import com.lasrosas.iot.core.flux.DigitalTwinTestConfig.TelemetryGateway;
import com.lasrosas.iot.core.ingestor.parsers.api.SensorConfig;
import com.lasrosas.iot.core.shared.telemetry.DistanceMeasurement;
import com.lasrosas.iot.core.shared.telemetry.Telemetry;
import com.lasrosas.iot.core.shared.telemetry.WaterTankFilling;
import com.lasrosas.iot.core.shared.telemetry.WaterTankStatus;
import com.lasrosas.iot.core.shared.utils.LasRosasHeaders;

@EnableIntegration
@ContextConfiguration(classes = { DigitalTwinTestConfig.class, SensorConfig.class})
@IntegrationComponentScan(basePackages = "com.lasrosas.iot.core.flux")
public class DigitalTwinTest extends BaseDatabaseTest {
	public static Logger log = LoggerFactory.getLogger(DigitalTwinTest.class);

	@Autowired
	private ThingLoraRepo thingRepo;

	@Autowired
	private TelemetryGateway telemetryGateway;

	@Autowired
	private PublishSubscribeChannel telemetryChannel;

	@Autowired
	private PollableChannel orderChannel;

	@Autowired
	private PollableChannel downlinkChannel;

	@Autowired
	private PollableChannel errorChannel;

	@Autowired
	private Gson gson;

	@Test
	@DirtiesContext
	@Transactional
	public void test_WanterTank() {

		var distanceSensor = thingRepo.getByDeveui(SampleData.WATER_TANK_DISTANCE_SENSOR_DEVEUI).get();

		final var expectedValues = new WaterTankFilling[] {
				new WaterTankFilling(WaterTankStatus.FULL, 3.141592653589793, 100.0),
				new WaterTankFilling(WaterTankStatus.FULL, 3.0828667467121913, 98.13069632657506),
				new WaterTankFilling(WaterTankStatus.NORMAL, 2.8460938134036495, 90.59397978129064),
				new WaterTankFilling(WaterTankStatus.NORMAL, 2.5274078042854153, 80.44988905221149),
				new WaterTankFilling(WaterTankStatus.NORMAL, 2.1616707412353775, 68.80811676094635),
				new WaterTankFilling(WaterTankStatus.NORMAL, 1.7704624916671186, 56.355571421523095),
				new WaterTankFilling(WaterTankStatus.WARNING, 1.3711301619226752, 43.644428578476926),
				new WaterTankFilling(WaterTankStatus.WARNING, 0.9799219123544161, 31.19188323905367),
				new WaterTankFilling(WaterTankStatus.ALARM, 0.6141848493043787, 19.55011094778854),
				new WaterTankFilling(WaterTankStatus.ALARM, 0.2954988401861444, 9.406020218709378),
				new WaterTankFilling(WaterTankStatus.EMPTY, 0.058725906877602096, 1.8693036734249415)
		};

		final var i = new int[] {0};

		telemetryChannel.subscribe((m) -> {

			if(m.getPayload() instanceof DistanceMeasurement) return;

			var expected = expectedValues[i[0]++];
			var received = (WaterTankFilling)m.getPayload();
			assertEquals(expected.getVolume(), received.getVolume());
			assertEquals(expected.getPercentage(), received.getPercentage());
		});

		var time = LocalDateTime.now();
		for(var d = 0.0; d < 2.0; d += 0.2) {
			var distance = new DistanceMeasurement(d);
			var imessage = MessageBuilder.withPayload((Telemetry)distance)
					.setHeader(LasRosasHeaders.THING_ID, distanceSensor.getTechid())
					.setHeader(LasRosasHeaders.TIME_RECEIVED, time)
					.build();
			telemetryGateway.sendTelemetry(imessage);
			time = time.plusHours(1);
		}

		assertEquals(i[0], expectedValues.length);
	}

	@Test
	@DirtiesContext
	@Transactional
	public void test_MultiSwitch() throws Exception {

		var multiswitchSensor = thingRepo.getByDeveui(SampleData.MULTISWITCH_SENSOR_DEVEUI).get();
		var sensorTechid = multiswitchSensor.getTechid();
		var time = LocalDateTime.now();

		// JOIN
/*
		var imessage = MessageBuilder.withPayload((StateMessage)connectionState)
					.setHeader(LasRosasHeaders.THING_ID, sensorTechid)
					.setHeader(LasRosasHeaders.TIME_RECEIVED, time)
					.build();
		telemetryGateway.sendTelemetry(imessage);

		Thread.sleep(1000);

		var errorMessage = errorChannel.receive(200);
		assertNull(errorMessage);

		var orderMessage = orderChannel.receive(1000);
		assertNull(orderMessage);

		var downlinkMessage = downlinkChannel.receive(1000);

		assertNotNull(downlinkMessage);
		log.info(gson.toJson(downlinkMessage.getPayload()));
		assertTrue(downlinkMessage.getPayload() instanceof String);
		var download = (String)downlinkMessage.getPayload();
		assertEquals("{\"confirmed\": true, \"fPort\": 1, \"data\": \"04000100000000000000\"}", download);
*/
	}
}

