package com.lasrosas.iot.flux;

import static org.junit.jupiter.api.Assertions.assertEquals;

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
import org.springframework.messaging.Message;
import org.springframework.messaging.PollableChannel;
import org.springframework.test.context.ContextConfiguration;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.lasrosas.iot.database.IOTDatabaseConfig;
import com.lasrosas.iot.ingestor.services.lora.api.LoraMessageUplink;
import com.lasrosas.iot.ingestor.services.lora.api.LoraMetricMessage;
import com.lasrosas.iot.ingestor.services.sensors.api.ThingEncodedMessage;
import com.lasrosas.iot.ingestor.shared.LasRosasHeaders;
import com.lasrosas.iot.shared.utils.UtilsConfig;

@EnableIntegration
@ContextConfiguration(classes = {LoraMessageSplitterConfig.class, IOTDatabaseConfig.class, UtilsConfig.class})
@DataJpaTest()
@AutoConfigureTestDatabase(replace = Replace.NONE)
@EntityScan("com.lasrosas.iot")
public class LoraMessageSplitterTest {
	public static Logger log = LoggerFactory.getLogger(Rak7249FluxLoraTransformerTest.class);

	@Autowired
	private DirectChannel inputChannel;

	@Autowired
	private PollableChannel loraMetricChannel;

	@Autowired
	private PollableChannel thingEncodedChannel;
	
	private Gson gson = new GsonBuilder().setPrettyPrinting().create();

	@Test
	public void HandleLoraMessage() {
		var message = new LoraMessageUplink();
		message.setData("data");
		message.setDataEncoding("encoding");
		message.setDeveui("1111111111111111");
		message.setCnt(123);
		message.setFrequency(456L);
		message.setPort(678);
		message.setRssi(321);
		message.setSnr(1.3F);

		var imessage = MessageBuilder.withPayload(message).build();

		inputChannel.send(imessage);

		var result = loraMetricChannel.receive(5000);
		log.info(gson.toJson(result));

		var loraMetric = (LoraMetricMessage)result.getPayload();
		assertEquals(message.getCnt(), loraMetric.getCnt());
		assertEquals(message.getPort(), loraMetric.getPort());
		assertEquals(message.getRssi(), loraMetric.getRssi());
		assertEquals(message.getSnr(), loraMetric.getSnr());

		@SuppressWarnings("unchecked")
		var thingEncoded = (Message<ThingEncodedMessage>)thingEncodedChannel.receive(5000);

		log.info(gson.toJson(thingEncoded));

		assertEquals(LasRosasHeaders.time(imessage), LasRosasHeaders.time(thingEncoded));
		assertEquals(imessage.getPayload().getData(), thingEncoded.getPayload().getEncodedData());
		assertEquals(imessage.getPayload().getDataEncoding(), thingEncoded.getPayload().getDataEncoding());
	}
}
