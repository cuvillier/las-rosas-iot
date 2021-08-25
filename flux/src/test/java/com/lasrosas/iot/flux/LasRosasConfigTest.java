package com.lasrosas.iot.flux;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.integration.annotation.IntegrationComponentScan;
import org.springframework.integration.config.EnableIntegration;
import org.springframework.test.context.ContextConfiguration;

import com.lasrosas.iot.database.IOTDatabaseConfig;
import com.lasrosas.iot.flux.LasRosasIotConfig.LasRosasGateway;
import com.lasrosas.iot.ingestor.services.rak7249.api.Rak7249MessageRx;
import com.lasrosas.iot.ingestor.services.rak7249.api.Rak7249MessageRx.RxInfo;
import com.lasrosas.iot.ingestor.services.rak7249.api.Rak7249MessageRx.TxInfo;
import com.lasrosas.iot.ingestor.services.sensors.impl.SensorsConfig;
import com.lasrosas.iot.shared.utils.UtilsConfig;

@EnableIntegration
@ContextConfiguration(classes = { LasRosasIotConfig.class, SensorsConfig.class, IOTDatabaseConfig.class,
		UtilsConfig.class })
@DataJpaTest()
@IntegrationComponentScan
@AutoConfigureTestDatabase(replace = Replace.NONE)
@EntityScan("com.lasrosas.iot")
public class LasRosasConfigTest {
	public static Logger log = LoggerFactory.getLogger(LasRosasConfigTest.class);

	@Autowired
	private LasRosasGateway LasRosasGateway;

	@Test
	public void test() throws Exception {
		try {
			var message = new Rak7249MessageRx();
			message.setApplicationID(1);
			message.setApplicationName("rak7249");
			message.setTime(LocalDateTime.now());
			message.setData("AQDWAlYD/PzFBw5bDgUHDwAUAA804w==");
			message.setData_encode("base64");
			message.setDevEUI("a81758fffe053159");
			message.setDeviceName("Elsys/ELT2-MB7389/A81758FFFE053159");
			message.setFCnt(1);
			message.setFPort(1);

			var rxInfo = new RxInfo();
			rxInfo.setGatewayID("rak7249");
			rxInfo.setLoRaSNR(1.234f);
			rxInfo.setRssi(80);
			rxInfo.setTime(LocalDateTime.now());
			message.addRxInfo(rxInfo);

			var txInfo = new TxInfo();
			txInfo.setDr(4);
			txInfo.setFrequency(123456L);
			message.setTxInfo(txInfo);

			LasRosasGateway.sendRak7149(message);

			log.info("Ok");
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
}
