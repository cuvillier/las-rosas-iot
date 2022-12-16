package com.lasrosas.iot.core.flux;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.core.annotation.Order;
import org.springframework.integration.annotation.IntegrationComponentScan;
import org.springframework.integration.config.EnableIntegration;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

import com.google.gson.Gson;
import com.lasrosas.iot.core.database.IOTDatabaseConfig;
import com.lasrosas.iot.core.flux.LasRosasFluxDelegate.LasRosasGateway;
import com.lasrosas.iot.core.ingestor.parsers.impl.SensorsConfig;
import com.lasrosas.iot.core.shared.telemetry.MultiSwitchOrder;
import com.lasrosas.iot.core.shared.utils.LasRosasHeaders;
import com.lasrosas.iot.core.shared.utils.UtilsConfig;

@EnableIntegration
@ContextConfiguration(classes = { LasRosasIotConfig.class, SensorsConfig.class, IOTDatabaseConfig.class,
		UtilsConfig.class })
@DataJpaTest()
@IntegrationComponentScan
@AutoConfigureTestDatabase(replace = Replace.NONE)
@EntityScan("com.lasrosas.iot")
@ActiveProfiles("test")
public class LasRosasConfigTest {
	public static Logger log = LoggerFactory.getLogger(LasRosasConfigTest.class);

	@Autowired
	private LasRosasGateway lasRosasGateway;

	@SuppressWarnings("unchecked")
	@Test
	public void test() throws Exception {
		try {
			String json = FluxTestData.RAK7942_MESSAGE;

			var imessage = MessageBuilder.withPayload(json).setHeader("mqtt_receivedTopic", "application/1/device/1/rx").build();

			lasRosasGateway.sendRak7249(imessage);
	
			var order = new MultiSwitchOrder(0, "0");
			var iorder = MessageBuilder
					.withPayload(order)
					.setHeader(LasRosasHeaders.GATEWAY_NAURAL_ID, "GatewayTechoFinca")
					.setHeader(LasRosasHeaders.THING_ID, 3L)	// 3L is the Test activator Lora sensor
					.build();
			
			// Keep the double cast for mac jdk to build with maven.
			lasRosasGateway.sendOrder((Message<? extends Order>) (Message<?>)iorder);

			log.info("Ok");
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
}
