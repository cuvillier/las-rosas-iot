package com.lasrosas.iot.core.flux;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.log;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.integration.channel.PublishSubscribeChannel;
import org.springframework.integration.config.EnableIntegration;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.MessageChannel;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.util.Assert;

import com.google.gson.Gson;
import com.lasrosas.iot.core.database.IOTDatabaseConfig;
import com.lasrosas.iot.core.ingestor.gateway.impl.rak7249.api.Rak7249MessageAck;
import com.lasrosas.iot.core.ingestor.gateway.impl.rak7249.api.Rak7249MessageJoin;
import com.lasrosas.iot.core.ingestor.lora.api.LoraMessageAck;
import com.lasrosas.iot.core.ingestor.lora.api.LoraMessageJoin;
import com.lasrosas.iot.core.ingestor.lora.api.LoraMessageUplink;
import com.lasrosas.iot.core.ingestor.parsers.api.ThingEncodedMessage;
import com.lasrosas.iot.core.ingestor.parsers.impl.SensorsConfig;
import com.lasrosas.iot.core.shared.utils.LasRosasHeaders;
import com.lasrosas.iot.core.shared.utils.UtilsConfig;

/*
assertEquals(1, frame0x30.getStatus().getFrameCounter());
assertFalse(frame0x30.getStatus().isConfig());
assertFalse(frame0x30.getStatus().isLowBat());
assertFalse(frame0x30.getStatus().isTimestamp());

assertEquals(ChannelState.OPEN_OFF, frame0x30.getChannel1State());
assertEquals(ChannelState.OPEN_OFF, frame0x30.getChannel2State());
assertEquals(ChannelState.OPEN_OFF, frame0x30.getChannel3State());
assertEquals(ChannelState.OPEN_OFF, frame0x30.getChannel4State());

assertEquals(33, frame0x30.getChannel1EventCounter());
assertEquals(0, frame0x30.getChannel2EventCounter());
assertEquals(0, frame0x30.getChannel3EventCounter());
assertEquals(0, frame0x30.getChannel4EventCounter());

assertNull(frame0x30.getTimestamp());
*/

@EnableIntegration
@ContextConfiguration(
		classes = {
				HandleThingMessageConfig.class,
				LasRosasIotBaseConfig.class,
				SensorsConfig.class,
				IOTDatabaseConfig.class,
				UtilsConfig.class
		}
)
@DataJpaTest()
@AutoConfigureTestDatabase(replace = Replace.NONE)
@EntityScan("com.lasrosas.iot")
@ActiveProfiles("test")
public class HandleThingMessageTest {
	public static Logger log = LoggerFactory.getLogger(HandleThingMessageTest.class);

	@Autowired
	private MessageChannel loraChannel;

	@Autowired
	private PublishSubscribeChannel thingEncodedChannel;
	@Autowired
	private PublishSubscribeChannel loraMetricChannelName;
	
	@Autowired
	private PublishSubscribeChannel stateChannelName;
	
	@Autowired
	private PublishSubscribeChannel errorChannelName;

	@Autowired
	private Gson gson;

	private enum HandlerResult {
		OK,
		ERROR,
		WAITING
	};
	private HandlerResult handlerResult;

	/*
	 * Test Uplink LoraMessage handling
	 * @precondition:
	 *  - thing with deveui 0018b2200000093c
	 **/
	@Test
	@DirtiesContext
	public void rak7249ToLoraMessageUplink() {

		final var loraMessage = new LoraMessageUplink();
		loraMessage.setData("MCAAIQAAAAAAAAA=");
		loraMessage.setDataEncoding("base64");
		loraMessage.setCnt(0);
		loraMessage.setDeveui("0018b2200000093c");
		loraMessage.setFrequency(null);
		loraMessage.setPort(0);
		loraMessage.setRssi(null);
		loraMessage.setSnr(null);

		var imessage = MessageBuilder
				.withPayload(loraMessage)
				.setHeader(LasRosasHeaders.THING_ID, 6L)
				.setHeader(LasRosasHeaders.TIME_RECEIVED, LocalDateTime.now())
				.build();

		handlerResult = HandlerResult.WAITING;
/*
		router.setChannelMapping(ThingEncodedMessage.class.getName(), LasRosasIotBaseConfig.thingEncodedDataChannelName);
		router.setChannelMapping(LoraMetricMessage.class.getName(), LasRosasIotBaseConfig.loraMetricChannelName);
		router.setChannelMapping(ConnectionState.class.getName(), LasRosasIotBaseConfig.stateChannelName);
		router.setChannelMapping(StillAlive.class.getName(), LasRosasIotBaseConfig.stateChannelName);
		router.setDefaultOutputChannelName(LasRosasIotBaseConfig.errorChannelName);
*/

		thingEncodedChannel.subscribe((m) -> {
			try {
				log.info(gson.toJson(m));

				var thingEncodedMessage = (ThingEncodedMessage)m.getPayload();
	
				log.info(gson.toJson(loraMessage));
				assertEquals(loraMessage.getData(), thingEncodedMessage.getEncodedData());

			} catch(RuntimeException e) {
				handlerResult = HandlerResult.ERROR;
				throw e; 	// Back to main Thread.
			}
			handlerResult = HandlerResult.OK;
		});

		//.send(imessage);

		if(handlerResult != HandlerResult.OK) throw new RuntimeException("Test failed: result = " + handlerResult);

	}

	@Test
	@DirtiesContext
	public void rak7249ToLoraMessageJoin() {
/*
		final var messageRak7249 = new Rak7249MessageJoin();
		messageRak7249.setDeviceName("Adenuis/ARF8180BA/0018B2200000093C");
		messageRak7249.setApplicationID("1");
		messageRak7249.setApplicationName("LasRosas");
		messageRak7249.setDevAddr("1234");
		messageRak7249.setDevEUI("0018b2200000093c");

		var imessage = MessageBuilder
				.withPayload(messageRak7249)
				.setHeader(LasRosasHeaders.THING_ID, 6L)
				.setHeader(LasRosasHeaders.TIME_RECEIVED, LocalDateTime.now())
				.build();

		handlerResult = HandlerResult.WAITING;

		loraChannel.subscribe((m) -> {
			try {
				log.info(gson.toJson(m));

				var loraMessage = (LoraMessageJoin)m.getPayload();

				log.info(gson.toJson(loraMessage));
				assertEquals(messageRak7249.getApplicationName(), loraMessage.getGatewayId());
				assertEquals(messageRak7249.getDevEUI(), loraMessage.getDeveui());
				assertEquals("Adenuis", loraMessage.getManufacturer());
				assertEquals("ARF8180BA", loraMessage.getModel());

			} catch(RuntimeException e) {
				handlerResult = HandlerResult.ERROR;
				throw e; 	// Back to main Thread.
			}
			handlerResult = HandlerResult.OK;
		});

		rak7249UplinkTxChannel.send(imessage);

		if(handlerResult != HandlerResult.OK) throw new RuntimeException("Test failed: result = " + handlerResult);
*/
	}

	@Test
	@DirtiesContext
	public void rak7249ToLoraMessageAck() {
/*
		final var messageRak7249 = new Rak7249MessageAck();
		messageRak7249.setDeviceName("Adenuis/ARF8180BA/0018B2200000093C");
		messageRak7249.setApplicationID("1");
		messageRak7249.setApplicationName("LasRosas");
		messageRak7249.setDevEUI("0018b2200000093c");
		messageRak7249.setAcknowledge(true);
		messageRak7249.setfCnt(1);
		messageRak7249.setTime(LocalDateTime.now());

		var imessage = MessageBuilder
				.withPayload(messageRak7249)
				.setHeader(LasRosasHeaders.THING_ID, 6L)
				.setHeader(LasRosasHeaders.TIME_RECEIVED, LocalDateTime.now())
				.build();

		handlerResult = HandlerResult.WAITING;

		loraChannel.subscribe((m) -> {
			try {
				log.info(gson.toJson(m));

				var loraMessage = (LoraMessageAck)m.getPayload();

				log.info(gson.toJson(loraMessage));
				assertEquals(messageRak7249.getApplicationName(), loraMessage.getGatewayId());
				assertEquals(messageRak7249.getDevEUI(), loraMessage.getDeveui());
				assertEquals(messageRak7249.getApplicationName(), loraMessage.getGatewayId());

			} catch(RuntimeException e) {
				handlerResult = HandlerResult.ERROR;
				throw e; 	// Back to main Thread.
			}
			handlerResult = HandlerResult.OK;
		});

		rak7249UplinkTxChannel.send(imessage);

		if(handlerResult != HandlerResult.OK) throw new RuntimeException("Test failed: result = " + handlerResult);
*/
	}
}
