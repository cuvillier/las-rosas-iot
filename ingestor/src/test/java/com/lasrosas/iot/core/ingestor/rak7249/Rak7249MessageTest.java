package com.lasrosas.iot.core.ingestor.rak7249;

import org.junit.jupiter.api.Test;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.util.Assert;

import com.lasrosas.iot.core.ingestor.gateway.impl.rak7249.api.Rak7249MessageRx;
import com.lasrosas.iot.core.shared.utils.GsonUtils;
import com.lasrosas.iot.core.shared.utils.UtilsConfig;

@ContextConfiguration(classes = {UtilsConfig.class})
public class Rak7249MessageTest {

	@Test
	public void jsonToRX() {
		String json =
			"		{\n"
			+ "			\"applicationID\": \"3\",\n"
			+ "			\"applicationName\": \"las-rosas-iot\",\n"
			+ "			\"devEUI\": \"0018b2200000093c\",\n"
			+ "			\"deviceName\": \"Adenuis/ARF8180BA/0018B2200000093C\",\n"
			+ "			\"timestamp\": 1620078842,\n"
			+ "			\"fCnt\": 6694,\n"
			+ "			\"fPort\": 1,\n"
			+ "			\"data\": \"Q6ABAP8CAPw=\",\n"
			+ "			\"data_encode\": \"base64\",\n"
			+ "			\"adr\": true,\n"
			+ "			\"rxInfo\": [\n"
			+ "				{\n"
			+ "					\"gatewayID\": \"60c5a8fffe76f8b2\",\n"
			+ "					\"loRaSNR\": 8.3,\n"
			+ "					\"rssi\": -70,\n"
			+ "					\"location\": {\n"
			+ "						\"latitude\": 36.825600,\n"
			+ "						\"longitude\": -5.579390,\n"
			+ "						\"altitude\": 279\n"
			+ "					},\n"
			+ "					\"time\": \"2021-07-18T07:50:17\"\n"
			+ "				}\n"
			+ "			],\n"
			+ "			\"txInfo\": {\n"
			+ "				\"frequency\": 868500000,\n"
			+ "				\"dr\": 0\n"
			+ "			}\n"
			+ "		}";

		var  gson = GsonUtils.gson();
//		Gson gson = new GsonBuilder().create();

		var message = gson.fromJson(json, Rak7249MessageRx.class);

		Assert.notNull(message, "message is null");
		System.out.println(gson.toJson(message));
		Assert.notNull(message.getDevEUI(), "devEUI is null");
	}
}
