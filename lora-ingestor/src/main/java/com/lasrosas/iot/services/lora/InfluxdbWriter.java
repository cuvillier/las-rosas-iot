package com.lasrosas.iot.services.lora;

import java.sql.Timestamp;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.influxdb.InfluxDB;
import org.influxdb.InfluxDBFactory;
import org.influxdb.dto.Point;
import org.influxdb.dto.Point.Builder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.lasrosas.iot.services.db.entities.thg.ThingLora;
import com.lasrosas.iot.services.db.entities.tsr.TimeSeriePoint;

public class InfluxdbWriter {
	public static Logger log = LoggerFactory.getLogger(InfluxdbWriter.class);

	private final static String DATABASE_NAME= "lasrosasiot";

	@Autowired
	private Gson gson;

	private String serverURL = "http://127.0.0.1:8086";
	private String username = "lasrosas";
	private String password = "lasrosas";
	private InfluxDB influxDB;

	public void send(TimeSeriePoint point) {

		influxDB = InfluxDBFactory.connect(serverURL, username, password);
		
		try {
			influxDB.setDatabase(DATABASE_NAME);
	
			ThingLora thing = (ThingLora)point.getTimeSerie().getThing();
			String measurement = "lora." + thing.getDeveui() + "." + point.getTimeSerie().getType().getSchema();
			if(point.getTimeSerie().getSensor() != null)
				measurement += "." + point.getTimeSerie().getSensor();
	
			var jsono = gson.fromJson(point.getValue(), JsonObject.class);
			
			var influxPointBuilder = Point.measurement(measurement)
				    .time(Timestamp.valueOf(point.getTime()).getTime(), TimeUnit.MILLISECONDS);

			addFields(influxPointBuilder, "", jsono);
	
			var influxPoint = influxPointBuilder.build();
			influxDB.write(influxPoint);

		} finally {
			influxDB.close();
		}
	}

	private void addFields(Builder influxPointBuilder, String prefix, JsonObject jsono) {

		for(Map.Entry<String, JsonElement> entry : jsono.entrySet()) {
			final var value =  entry.getValue();
			final var key = prefix + entry.getKey();

			if( value.isJsonObject() ) {

				addFields(influxPointBuilder, key + ".", value.getAsJsonObject());

			} else if( value.isJsonPrimitive()) {
				final var primitive = value.getAsJsonPrimitive();

				// Influx does not taste . in keys while eating the point...
				String keyInflux = key.replace('.', '-');
				String strValue = primitive.getAsString();

				if( primitive.isNumber()) {
					var number = primitive.getAsNumber();

					if( strValue.contains(".")|| strValue.contains(","))
						influxPointBuilder.addField(keyInflux, primitive.getAsFloat());
					else
						influxPointBuilder.addField(keyInflux, number);

				} else if(primitive.isString()) {
					influxPointBuilder.addField(keyInflux, strValue);
				}
			}
		}
	}
}
