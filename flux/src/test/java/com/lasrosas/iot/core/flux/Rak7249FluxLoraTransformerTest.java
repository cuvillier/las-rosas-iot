package com.lasrosas.iot.core.flux;

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

import com.lasrosas.iot.core.ingestor.gateway.impl.rak7249.api.Rak7249MessageJoin;
import com.lasrosas.iot.core.ingestor.gateway.impl.rak7249.api.Rak7249MessageRx;
import com.lasrosas.iot.core.ingestor.gateway.impl.rak7249.api.Rak7249MessageRx.RxInfo;
import com.lasrosas.iot.core.ingestor.gateway.impl.rak7249.api.Rak7249MessageRx.TxInfo;
import com.lasrosas.iot.core.ingestor.lora.api.LoraMessageJoin;
import com.lasrosas.iot.core.ingestor.lora.api.LoraMessageUplink;
import com.lasrosas.iot.core.shared.utils.LasRosasHeaders;
import com.lasrosas.iot.core.shared.utils.TimeUtils;
import com.lasrosas.iot.core.shared.utils.UtilsConfig;

@SpringBootTest
@EnableIntegration
@ContextConfiguration(classes = {Rak7249FluxLoraTransformerConfig.class, UtilsConfig.class})
public class Rak7249FluxLoraTransformerTest {
	public static Logger log = LoggerFactory.getLogger(Rak7249FluxLoraTransformerTest.class);

	@Autowired
	private DirectChannel inputChannel;

	@Autowired
	private PollableChannel outputChannel;

	@Test
	public void Rak7249MessageRx() {
		var rak = new Rak7249MessageRx();
		rak.setDevEUI("deveui");
		rak.setTimestamp(1234567L);
		rak.setApplicationName("applicationName");
		rak.setDeviceName("deviceName");
		rak.setData("glop");
		rak.setData_encode("base64");
		rak.setDevEUI("DevEUI");
		rak.setFCnt(123);
		rak.setFPort(345);
		rak.addRxInfo(new RxInfo("gatewayId", 123.456F, 123, LocalDateTime.now()));
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
		assertEquals(rak.getData_encode(), lora.getDataEncoding());
		assertEquals(rak.getTxInfo().getFrequency(), lora.getFrequency());
		assertEquals(rak.getFPort(), lora.getPort());
		assertEquals(rak.getRxInfo().get(0).getRssi(), lora.getRssi());
		assertEquals(rak.getRxInfo().get(0).getLoRaSNR(), lora.getSnr());
		assertEquals(LasRosasHeaders.time(message), LasRosasHeaders.time(result));
	}

	@Test
	public void Rak7249MessageJoin() {

		var rak = new Rak7249MessageJoin();
		rak.setDevEUI("deveui");
		rak.setTime(LocalDateTime.now());
		rak.setApplicationName("applicationName");
		rak.setDeviceName("deviceName");
		rak.setDevEUI("DevEUI");

		var message = MessageBuilder.withPayload(rak).build();

		inputChannel.send(message);

		@SuppressWarnings("unchecked")
		var result = (Message<LoraMessageJoin>)outputChannel.receive(5000);
		var lora = result.getPayload();

		assertEquals(rak.getDevEUI(), lora.getDeveui());
	}
}
