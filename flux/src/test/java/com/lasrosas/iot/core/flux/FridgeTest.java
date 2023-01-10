package com.lasrosas.iot.core.flux;

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
import com.lasrosas.iot.core.database.repo.DigitalTwinRepo;
import com.lasrosas.iot.core.database.repo.ThingLoraRepo;
import com.lasrosas.iot.core.database.twins.Fridge;
import com.lasrosas.iot.core.flux.DigitalTwinTestConfig.TelemetryGateway;
import com.lasrosas.iot.core.shared.telemetry.AirEnvironment;
import com.lasrosas.iot.core.shared.telemetry.Telemetry;
import com.lasrosas.iot.core.shared.utils.LasRosasHeaders;

@EnableIntegration
@ContextConfiguration(classes = { FridgeTestConfig.class})
@IntegrationComponentScan("com.lasrosas.iot.core")
public class FridgeTest extends BaseDatabaseTest {
	public static Logger log = LoggerFactory.getLogger(FridgeTest.class);

	@Autowired
	private ThingLoraRepo thingRepo;

	@Autowired
	private DigitalTwinRepo dtwinRepo;

	@Autowired
	private TelemetryGateway telemetryGateway;

	@Autowired
	private PublishSubscribeChannel telemetryChannel;

	@Autowired
	private Gson gson;

	@Test
	@DirtiesContext
	@Transactional
	public void test_Fridge() {
		var twinSensor = thingRepo.findByDeveui(SampleData.FRIDGE_TEMPERATURE_SENSOR_DEVEUI).get();
		var fridge = (Fridge)dtwinRepo.getByName(SampleData.FRIDGE_NAME);

		telemetryChannel.subscribe((m) -> {
			log.info("Received-----------------------");
			log.info(gson.toJson(m.getPayload()));
		});

		var airEnvironment = new AirEnvironment(5.0, 50.0, 20.0);
		var imessage = MessageBuilder.withPayload((Telemetry)airEnvironment)
				.setHeader(LasRosasHeaders.THING_ID, twinSensor.getTechid()).build();

		telemetryGateway.sendTelemetry(imessage);

		// Send an alarm
		airEnvironment = new AirEnvironment(fridge.getInsideTempMax() + 1, 50.0, 20.0);
		imessage = MessageBuilder.withPayload((Telemetry)airEnvironment)
				.setHeader(LasRosasHeaders.THING_ID, twinSensor.getTechid()).build();

		telemetryGateway.sendTelemetry(imessage);
	}
}

