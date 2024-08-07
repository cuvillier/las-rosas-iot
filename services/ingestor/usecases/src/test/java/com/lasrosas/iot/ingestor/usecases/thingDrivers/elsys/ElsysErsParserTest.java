package com.lasrosas.iot.ingestor.usecases.thingDrivers.elsys;

import com.lasrosas.iot.ingestor.shared.JsonUtils;
import com.lasrosas.iot.ingestor.usecases.handleLorawanMessages.LorawanMessageUplinkRx;
import com.lasrosas.iot.ingestor.usecases.handleLorawanMessages.thingDrivers.elsys.ElsysGenericDriver;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ElsysErsParserTest {
	public static final Logger log = LoggerFactory.getLogger(ElsysErsParserTest.class);
	private ElsysGenericDriver parser = new ElsysGenericDriver();

	@Test
	public void test() {
		var message = LorawanMessageUplinkRx.builder()
				.data("AQDmAj8EAAoFAAcOEw")
				.dataEncode(LorawanMessageUplinkRx.BASE64)
				.build();

		var result = parser.decodeUplink(message);
		log.info(JsonUtils.toJson(result));
	}
}
