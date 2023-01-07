package com.lasrosas.iot.core.ingestor;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

import com.google.gson.Gson;
import com.lasrosas.iot.core.ingestor.gateway.impl.rak7249.api.Rak7249MessageRx;
import com.lasrosas.iot.core.shared.utils.UtilsConfig;

@SpringBootTest
@ContextConfiguration(classes=UtilsConfig.class)
public class NonRegresionTest {

	@Autowired
	private Gson gson;

	@Test
	public void exceptionFromJson() {
		var json = "{\"applicationID\":\"1\",\"applicationName\":\"las-rosas-iot\",\"devEUI\":\"a8404111118446ed\",\"deviceName\":\"DRAGINO/LHT65/a8404111118446ed\",\"timestamp\":1636109512,\"fCnt\":11032,\"fPort\":2,\"data\":\"y80F1QOSAQWRf/8=\",\"data_encode\":\"base64\",\"adr\":true,\"rxInfo\":[{\"gatewayID\":\"60c5a8fffe76f8b2\",\"loRaSNR\":0.0,\"rssi\":-90,\"location\":{\"latitude\":36.825740,\"longitude\":-5.579290,\"altitude\":337},\"time\":\"2023-01-02T15:54:01.315985Z\"}],\"txInfo\":{\"frequency\":868800000,\"dr\":7}}";

		gson.fromJson(json,  Rak7249MessageRx.class);
	}
}
