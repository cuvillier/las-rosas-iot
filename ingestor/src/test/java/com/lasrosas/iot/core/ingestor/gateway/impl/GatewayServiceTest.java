package com.lasrosas.iot.core.ingestor.gateway.impl;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import com.lasrosas.iot.core.database.entities.SampleData;
import com.lasrosas.iot.core.database.entities.dtw.BaseDatabaseTest;

@ContextConfiguration(classes = { GatewayServiceTestConfig.class})
public class GatewayServiceTest extends BaseDatabaseTest {

	@Autowired
	private GatewayServiceImpl service;

	@Test
	public void test() {
		byte[] payload = {0x01, 0x02, 0x03};
		service.encodeDownlink(SampleData.GatewayNaturalId, payload);
	}
}
