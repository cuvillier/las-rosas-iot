package com.lasrosas.iot.core.flux;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.time.LocalDateTime;
import java.util.Comparator;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.config.EnableIntegration;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.Message;
import org.springframework.messaging.PollableChannel;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.transaction.annotation.Transactional;

import com.google.gson.Gson;
import com.lasrosas.iot.core.database.entities.SampleData;
import com.lasrosas.iot.core.database.entities.dtw.BaseDatabaseTest;
import com.lasrosas.iot.core.ingestor.lora.api.LoraMessageUplink;
import com.lasrosas.iot.core.ingestor.lora.api.LoraMetricMessage;
import com.lasrosas.iot.core.shared.telemetry.AirEnvironment;
import com.lasrosas.iot.core.shared.telemetry.DistanceMeasurement;
import com.lasrosas.iot.core.shared.telemetry.StateMessage;
import com.lasrosas.iot.core.shared.telemetry.StillAlive;
import com.lasrosas.iot.core.shared.telemetry.Telemetry;
import com.lasrosas.iot.core.shared.utils.LasRosasHeaders;

@EnableIntegration
@ContextConfiguration(classes = { IngestionFluxTestConfig.class})
public class IngestionFluxTest extends BaseDatabaseTest {
	public static Logger log = LoggerFactory.getLogger(IngestionFluxTest.class);

	@Autowired
	private DirectChannel loraChannel;

	@Autowired
	private PollableChannel errorChannel;

	@Autowired
	private PollableChannel stateChannel;

	@Autowired
	private PollableChannel loraMetricChannel;

	@Autowired
	private PollableChannel telemetryChannel;

	@Autowired
	private Gson gson;

	@Test
	@DirtiesContext
	@Transactional
	public void test_ADEUNIS_ARF8180BA() {

		var loraMessageUplink = new LoraMessageUplink(
						SampleData.THING_ADEUNIS_ARF8180BA_DEVEUI,
						SampleData.THING_ADEUNIS_ARF8180BA_UPLINK1,
						"base64",
						678,
						123,
						321,
						1.3F,
						456L
					);

		var loraMetric = new LoraMetricMessage(
						loraMessageUplink.getPort(),
						loraMessageUplink.getCnt(),
						loraMessageUplink.getRssi(),
						loraMessageUplink.getSnr(),
						loraMessageUplink.getFrequency()
					);

		testLoraMessageUplink(loraMessageUplink, new Telemetry[] {
					new AirEnvironment(null, 844.8, null, null),
					new AirEnvironment(null, 0.0, null, null)
				},
				telemetryComp,
				new LoraMetricMessage[] {
						loraMetric
				},
				loraMetricComp,
				new StateMessage[] {
						new StillAlive()
				});
	}

	@Test
	@DirtiesContext
	@Transactional
	public void test_DRAGINO_LHT65() {

		var loraMessageUplink = new LoraMessageUplink(
						SampleData.THING_DRAGINO_LHT65_DEVEUI,
						SampleData.THING_DRAGINO_LHT65_UPLINK1,
						"base64",
						678,
						123,
						321,
						1.3F,
						456L
					);

		var loraMetric = new LoraMetricMessage(
						loraMessageUplink.getPort(),
						loraMessageUplink.getCnt(),
						loraMessageUplink.getRssi(),
						loraMessageUplink.getSnr(),
						loraMessageUplink.getFrequency()
					);

		testLoraMessageUplink(loraMessageUplink, new Telemetry[] {
					new AirEnvironment(null, 12.41, 86.3, null),
					new AirEnvironment(null, 11.56, null, null)
				},
				telemetryComp,
				new LoraMetricMessage[] {
						loraMetric
				},
				loraMetricComp,
				new StateMessage[] {
						new StillAlive()
				});
	}


	@Test
	@DirtiesContext
	@Transactional
	public void test_ELSYS_MB7389() {
		var loraMessageUplink = new LoraMessageUplink(
						SampleData.THING_ELSYS_MB7389_DEVEUI,
						SampleData.THING_ELSYS_MB7389_UPLINK1,
						"base64",
						678,
						123,
						321,
						1.3F,
						456L
					);

		var loraMetric = new LoraMetricMessage(
						loraMessageUplink.getPort(),
						loraMessageUplink.getCnt(),
						loraMessageUplink.getRssi(),
						loraMessageUplink.getSnr(),
						loraMessageUplink.getFrequency()
					);

		testLoraMessageUplink(loraMessageUplink, new Telemetry[] {
					new AirEnvironment(null, 10.2, 97.0, null),
					new DistanceMeasurement(0.3)
				},
				telemetryComp,
				new LoraMetricMessage[] {
						loraMetric
				},
				loraMetricComp,
				new StateMessage[] {
						new StillAlive()
				});
	}

	@Test
	@DirtiesContext
	@Transactional
	public void test_MFC88_LW13IO() {
		var loraMessageUplink = new LoraMessageUplink(
						SampleData.THING_MFC88_LW13IO_DEVEUI,
						SampleData.THING_MFC88_LW13IO_UPLINK1,
						"base64",
						678,
						123,
						321,
						1.3F,
						456L
					);

		var loraMetric = new LoraMetricMessage(
						loraMessageUplink.getPort(),
						loraMessageUplink.getCnt(),
						loraMessageUplink.getRssi(),
						loraMessageUplink.getSnr(),
						loraMessageUplink.getFrequency()
					);

		testLoraMessageUplink(loraMessageUplink, new Telemetry[] {
				},
				telemetryComp,
				new LoraMetricMessage[] {
						loraMetric
				},
				loraMetricComp,
				new StateMessage[] {
						new StillAlive()
				});
	}

	private static Comparator<LoraMetricMessage> loraMetricComp = new Comparator<LoraMetricMessage>() {

		@Override
		public int compare(LoraMetricMessage expected, LoraMetricMessage received) {
			assertEquals(expected.getCnt(), received.getCnt());
			assertEquals(expected.getPort(), received.getPort());
			assertEquals(expected.getRssi(), received.getRssi());
			assertEquals(expected.getSnr(), received.getSnr());
			assertEquals(expected.getFrequency(), received.getFrequency());
			return 1;
		}
	};

	private static Comparator<Telemetry> telemetryComp = new Comparator<Telemetry>() {

		@Override
		public int compare(Telemetry expected, Telemetry received) {

			if( expected instanceof AirEnvironment && received instanceof AirEnvironment ) {
				var expectedAirEnvironment = (AirEnvironment)expected;
				var receivedAirEnvironment = (AirEnvironment)received;
				assertEquals(expectedAirEnvironment.getHumidity(), receivedAirEnvironment.getHumidity());
				assertEquals(expectedAirEnvironment.getLight(), receivedAirEnvironment.getLight());
				assertEquals(expectedAirEnvironment.getSensor(), receivedAirEnvironment.getSensor());
				assertEquals(expectedAirEnvironment.getTemperature(), receivedAirEnvironment.getTemperature());
				return 1;
			}

			if( expected instanceof DistanceMeasurement && received instanceof DistanceMeasurement ) {
				var expectedAirEnvironment = (DistanceMeasurement)expected;
				var receivedAirEnvironment = (DistanceMeasurement)received;
				assertEquals(expectedAirEnvironment.getDistance(), receivedAirEnvironment.getDistance());
				return 1;
			}

			log.error("Cannot compare an " + expected.getClass().getSimpleName() + " and a " + received.getClass().getSimpleName());

			return 0;
		}
	};

	private void testLoraMessageUplink(
			LoraMessageUplink message,
			Telemetry[] telemetries,
			Comparator<Telemetry> telemetryComp,
			LoraMetricMessage[] loraMetricMessages,
			Comparator<LoraMetricMessage> loraMetricComp,
			StateMessage[] stateMessages) {
		try {

			var imessage = MessageBuilder.withPayload(message)
					.setHeader(LasRosasHeaders.TIME_RECEIVED, LocalDateTime.now()).build();

			loraChannel.send(imessage);
			Thread.sleep(100);

			Message<?> shouldBeNull;

			if ((shouldBeNull = errorChannel.receive(100)) != null) {
				var exception = (Exception)shouldBeNull.getPayload();
				throw exception;
			}

			for (var expected : telemetries) {
				var received = telemetryChannel.receive(500);

				assertNotNull(received);

				var receivedPayload = (Telemetry)received.getPayload();
				if( telemetryComp.compare(expected, receivedPayload) != 1) {
					log.error("Received value does not match excepted value");
					log.error("Received value : " + gson.toJson(receivedPayload));
					log.error("Expected value : " + gson.toJson(expected));
					throw new RuntimeException("Received value does not match excepted value");
				}

				log.info("Received payload " + receivedPayload.getClass().getSimpleName());
			}

			if ((shouldBeNull = telemetryChannel.receive(100)) != null) {
				var payload = shouldBeNull.getPayload();
				log.error("Unexpected message " + payload.getClass().getSimpleName());
				log.error(gson.toJson(payload));
				throw new RuntimeException("Too many messages from telemetryChannel");
			}

			for (var expected : loraMetricMessages) {

				var received = loraMetricChannel.receive(500);

				assertNotNull(received);

				var receivedPayload = (LoraMetricMessage)received.getPayload();
				assertEquals(1, loraMetricComp.compare(expected, receivedPayload));
				log.info("Received loraMetric" + receivedPayload.getClass().getSimpleName());
			}

			for (var expected : stateMessages) {
				var received = stateChannel.receive(500);
				assertNotNull(received);

				var receivedPayload = received.getPayload();
				assertEquals(expected.getClass(), receivedPayload.getClass());
				log.info("Received stateMessage" + receivedPayload.getClass().getSimpleName());
			}

			if ((shouldBeNull = stateChannel.receive(100)) != null) {
				var payload = shouldBeNull.getPayload();
				log.error("Unexpected message " + payload.getClass().getSimpleName());
				gson.toJson(payload);
				throw new RuntimeException("Too many messages from stateChannel");
			}

		} catch (RuntimeException e) {
			throw e;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
