package com.lasrosas.iot.flux;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDateTime;
import java.util.HashMap;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.config.EnableIntegration;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.Message;
import org.springframework.messaging.PollableChannel;
import org.springframework.test.context.ContextConfiguration;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.lasrosas.iot.ingestor.services.lora.api.LoraMessageUplink;
import com.lasrosas.iot.ingestor.services.rak7249.api.Rak7249MessageRx;
import com.lasrosas.iot.ingestor.services.rak7249.api.Rak7249MessageRx.RxInfo;
import com.lasrosas.iot.ingestor.services.rak7249.api.Rak7249MessageRx.TxInfo;
import com.lasrosas.iot.shared.utils.UtilsConfig;

@SpringBootTest
@EnableIntegration
@ContextConfiguration(classes = {Rak7249FluxLoraTransformerConfig.class, UtilsConfig.class})
public class Rak7249FluxLoraTransformerTest {
	public static Logger log = LoggerFactory.getLogger(Rak7249FluxLoraTransformerTest.class);

	@Autowired
	private DirectChannel inputChannel;

	@Autowired
	private PollableChannel outputChannel;

	private Gson gson = new GsonBuilder().create();

	@Test
	public void testRak7249FluxLoraTransformer() {
		log.trace("trace");
		log.debug("debug");
		log.info("info");
		log.warn("warn");
		log.error("error");

		var rak = new Rak7249MessageRx();
		rak.setDevEUI("deveui");
		rak.setTimestamp(1234567L);
		rak.setApplicationName("applicationName");
		rak.setDeviceName("deviceName");
		rak.setData("glop");
		rak.setDataEncode("base64");
		rak.setDevEUI("DevEUI");
		rak.setFCnt(123);
		rak.setFPort(345);
		rak.setRxInfo(new RxInfo("gatewayId", 123.456F, 123, LocalDateTime.now()));
		rak.setTxInfo(new TxInfo(123L, 123));

		var headers = new HashMap<String, String>();
		headers.put("name1", "value1");
		headers.put("name2", "value2");

		var message = MessageBuilder.withPayload(rak).copyHeaders(headers).build();

		inputChannel.send(message);

		@SuppressWarnings("unchecked")
		var result = (Message<LoraMessageUplink>)outputChannel.receive(5000);
		var lora = result.getPayload();

		assertEquals(rak.getDevEUI(), lora.getDeveui());
		assertEquals(rak.getFCnt(), lora.getCnt());
		assertEquals(rak.getData(), lora.getData());
		assertEquals(rak.getDataEncode(), lora.getDataEncoding());
		assertEquals(rak.getTxInfo().getFrequency(), lora.getFrequency());
		assertEquals(rak.getFPort(), lora.getPort());
		assertEquals(rak.getRxInfo().getRssi(), lora.getRssi());
		assertEquals(rak.getRxInfo().getLoRaSNR(), lora.getSnr());
		assertEquals(rak.getTimestamp(), lora.getTimestamp());
	}
}
