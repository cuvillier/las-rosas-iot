package com.lasrosas.iot.core.flux;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.config.EnableIntegration;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.PollableChannel;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.lasrosas.iot.core.database.IOTDatabaseConfig;
import com.lasrosas.iot.core.ingestor.parsers.api.ThingEncodedMessage;
import com.lasrosas.iot.core.ingestor.parsers.impl.SensorsConfig;
import com.lasrosas.iot.core.ingestor.parsers.impl.adeunis.AdeunisARF8170BAFrame.ChannelState;
import com.lasrosas.iot.core.ingestor.parsers.impl.adeunis.AdeunisARF8170BAFrame.UplinkFrame0x30;
import com.lasrosas.iot.core.shared.utils.LasRosasHeaders;
import com.lasrosas.iot.core.shared.utils.UtilsConfig;

@EnableIntegration
@ContextConfiguration(classes = {DecodeThingMessageTransformerConfig.class, SensorsConfig.class, IOTDatabaseConfig.class, UtilsConfig.class})
@DataJpaTest()
@AutoConfigureTestDatabase(replace = Replace.NONE)
@EntityScan("com.lasrosas.iot")
@ActiveProfiles("dev")
public class DecodeThingMessageTransformerTest {
	public static Logger log = LoggerFactory.getLogger(DecodeThingMessageTransformerTest.class);

	@Autowired
	private DirectChannel inputChannel;

	@Autowired
	private PollableChannel outputChannel;
	
	private Gson gson = new GsonBuilder().setPrettyPrinting().create();

	@Test
	public void DecodeThingMessageTransformer() {

		var message = new ThingEncodedMessage();
		message.setEncodedData("MCAAIQAAAAAAAAA=");
		message.setDataEncoding("base64");

		var imessage = MessageBuilder
				.withPayload(message)
				.setHeader(LasRosasHeaders.THING_ID, 6L)
				.setHeader(LasRosasHeaders.TIME_RECEIVED, LocalDateTime.now())
				.build();

		inputChannel.send(imessage);

		var result = outputChannel.receive(5000);

		log.info(gson.toJson(result));

		var frame0x30 = (UplinkFrame0x30)result.getPayload();

		log.info(gson.toJson(frame0x30));

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

	}
}
