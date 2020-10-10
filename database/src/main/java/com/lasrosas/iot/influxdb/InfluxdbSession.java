package com.lasrosas.iot.influxdb;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.validation.constraints.NotNull;

import org.influxdb.InfluxDB;
import org.influxdb.InfluxDBFactory;
import org.influxdb.dto.Point;
import org.influxdb.dto.Point.Builder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.lasrosas.iot.database.entities.dtw.DigitalTwin;
import com.lasrosas.iot.database.entities.thg.ThingLora;
import com.lasrosas.iot.database.entities.tsr.TimeSeriePoint;

@ConfigurationProperties(prefix = "sql")
@Validated
public class InfluxdbSession {
	public static Logger log = LoggerFactory.getLogger(InfluxdbSession.class);

	private static final int MIN_MILLI_BEWTWEEN_OPEN_ATTEMPT = 15 * 1000;

	@Autowired
	private Gson gson;

	@NotNull
	private String serverURL = "http://127.0.0.1:8086";

	@NotNull
	private String username;

	@NotNull
	private String password;

	@NotNull
	private String databaseName = "lasrosasiot";

	private InfluxDB influxDB;

	private long lastOpenAttempt = 0;

	public void write(List<TimeSeriePoint> points) {

		log.info("Write " + points.size() + " points to influxDB");

		for (var point : points) {
			write(point);
		}
	}

	public void write(TimeSeriePoint point) {
		String measurement;

		var thing = (ThingLora) point.getTimeSerie().getThing();
		var twin = point.getTimeSerie().getTwin();

		if(thing != null) 
			measurement= "lora." + thing.getDeveui() + "." + point.getTimeSerie().getType().getSchema();
		else
			measurement= "dtwin." + twin.getType().getSpace().getName() + "." + twin.getType().getName() + "." + twin.getName();

		if (point.getTimeSerie().getSensor() != null)
			measurement += "." + point.getTimeSerie().getSensor();

		var jsono = gson.fromJson(point.getValue(), JsonObject.class);

		var influxPointBuilder = Point.measurement(measurement).time(Timestamp.valueOf(point.getTime()).getTime(),
				TimeUnit.MILLISECONDS);

		addFields(influxPointBuilder, "", jsono);

		var influxPoint = influxPointBuilder.build();
		try {
			influxDB.write(influxPoint);
		} catch (Exception e) {
			closeIfNeeded();
			if (e instanceof RuntimeException)
				throw (RuntimeException) e;
			throw new RuntimeException(e);
		}
	}

	public void write(String measurement, LocalDateTime time, JsonObject jsono) {

		Builder influxPointBuilder = Point.measurement(measurement).time(Timestamp.valueOf(time).getTime(),
				TimeUnit.MILLISECONDS);

		addFields(influxPointBuilder, "", jsono);

		var influxPoint = influxPointBuilder.build();

		try {
			connectIfNeeded();
			influxDB.write(influxPoint);

		} catch (Exception e) {
			closeIfNeeded();
			if (e instanceof RuntimeException)
				throw (RuntimeException) e;
			throw new RuntimeException(e);
		}
	}

	private void addFields(Builder influxPointBuilder, String prefix, JsonObject jsono) {

		for (Map.Entry<String, JsonElement> entry : jsono.entrySet()) {
			final var value = entry.getValue();
			final var key = prefix + entry.getKey();

			if (value.isJsonObject()) {

				addFields(influxPointBuilder, key + ".", value.getAsJsonObject());

			} else if (value.isJsonPrimitive()) {
				final var primitive = value.getAsJsonPrimitive();

				// Influx does not taste . in keys while eating the point...
				String keyInflux = key.replace('.', '-');
				String strValue = primitive.getAsString();

				if (primitive.isNumber()) {
					var number = primitive.getAsNumber();

					if (strValue.contains(".") || strValue.contains(","))
						influxPointBuilder.addField(keyInflux, primitive.getAsFloat());
					else
						influxPointBuilder.addField(keyInflux, number);

				} else if (primitive.isString()) {
					influxPointBuilder.addField(keyInflux, strValue);
				}
			}
		}
	}

	@PostConstruct
	private void connectIfNeeded() {
		if (influxDB == null) {
			long now = System.currentTimeMillis();

			if (now - lastOpenAttempt < MIN_MILLI_BEWTWEEN_OPEN_ATTEMPT)
				throw new RuntimeException("Cannot connect yet, wait");

			lastOpenAttempt = now;

			influxDB = InfluxDBFactory.connect(serverURL, username, password);
			influxDB.setDatabase(databaseName);

			log.info("Connected to influxDB");
		}
	}

	@PreDestroy
	public void closeIfNeeded() {
		if (influxDB != null) {
			try {
				influxDB.close();
			} finally {
				influxDB = null;
			}
		}
	}

	public String getServerURL() {
		return serverURL;
	}

	public void setServerURL(String serverURL) {
		this.serverURL = serverURL;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

}
