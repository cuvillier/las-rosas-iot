package com.lasrosas.iot.ingestor.services.rak7249;

import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

import org.junit.jupiter.api.Test;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.lasrosas.iot.ingestor.services.rak7249.api.Rak7249MessageRx;

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
			+ "					\"time\": \"2021-07-18T07:50:17.653651Z\"\n"
			+ "				}\n"
			+ "			],\n"
			+ "			\"txInfo\": {\n"
			+ "				\"frequency\": 868500000,\n"
			+ "				\"dr\": 0\n"
			+ "			}\n"
			+ "		}";

		var  gson = new GsonBuilder().registerTypeAdapter(LocalDateTime.class, new JsonDeserializer<LocalDateTime>() { 
			@Override 
			public LocalDateTime deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException { 
				return ZonedDateTime.parse(json.getAsJsonPrimitive().getAsString()).toLocalDateTime();
			}}).create();

		gson.fromJson(json, Rak7249MessageRx.class);
	}
}
