package com.lasrosas.iot.influxdb;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.validation.constraints.NotNull;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.influxdb.client.InfluxDBClient;
import com.influxdb.client.InfluxDBClientFactory;
import com.influxdb.client.WriteApi;
import com.influxdb.client.domain.WritePrecision;
import com.influxdb.client.write.Point;
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
	private String token;

	@NotNull
	private String org;

	@NotNull
	private String bucket = "lasrosasiot";

	private InfluxDBClient influxDB;

	private long lastOpenAttempt = 0;

	public void write(List<TimeSeriePoint> points) {
		if(points.size() == 0) return;

		log.info("Write " + points.size() + " points to influxDB");

		for (var point : points) {
			write(point);
		}
	}

	public void write(TimeSeriePoint point) {
		String measurement;

		var thing = (ThingLora) point.getTimeSerie().getThing();
		var twin = point.getTimeSerie().getTwin();

		if(twin == null)
			measurement= "thing-lora-" + thing.getDeveui() + "-" + point.getTimeSerie().getType().getSchema();
		else
			measurement= "twin-" + twin.getType().getSpace().getName() + "-" + twin.getType().getName() + "-" + twin.getName();

		if (point.getTimeSerie().getSensor() != null)
			measurement += "-" + point.getTimeSerie().getSensor();

		var jsono = gson.fromJson(point.getValue(), JsonObject.class);

		var timelong = Timestamp.valueOf(point.getTime()).getTime();
		var influxdbPoint = Point.measurement(measurement.replace(" ", "").replace('.', '-')).time(timelong, WritePrecision.MS);

		addFields(influxdbPoint, "", jsono);

		connectIfNeeded();
		try (WriteApi writeApi = influxDB.getWriteApi()) {
			log.info("Write point to InfluxDB measurement=" + measurement);
			writeApi.writePoint(influxdbPoint);
		} catch (Exception e) {
			closeIfNeeded();
			if (e instanceof RuntimeException)
				throw (RuntimeException) e;
			throw new RuntimeException(e);
		}
	}

	public void write(String measurement, LocalDateTime time, JsonObject jsono) {

		long timestamp = Timestamp.valueOf(time).getTime();
		var influxPoint = Point.measurement(measurement).time(timestamp, WritePrecision.MS);

		addFields(influxPoint, "", jsono);

		connectIfNeeded();
		try (WriteApi writeApi = influxDB.getWriteApi()) {
			writeApi.writePoint(influxPoint);

		} catch (Exception e) {
			closeIfNeeded();
			if (e instanceof RuntimeException)
				throw (RuntimeException) e;
			throw new RuntimeException(e);
		}
	}

	private void addFields(Point point, String prefix, JsonObject jsono) {

		for (Map.Entry<String, JsonElement> entry : jsono.entrySet()) {
			final var value = entry.getValue();
			final var key = prefix + entry.getKey();

			if (value.isJsonObject()) {

				addFields(point, key + ".", value.getAsJsonObject());

			} else if (value.isJsonPrimitive()) {
				final var primitive = value.getAsJsonPrimitive();

				// Influx does not taste . in keys while eating the point...
				String keyInflux = key.replace('.', '-');
				String strValue = primitive.getAsString();

				if (primitive.isNumber()) {
					var number = primitive.getAsNumber();

					if (strValue.contains(".") || strValue.contains(","))
						point.addField(keyInflux, primitive.getAsFloat());
					else
						point.addField(keyInflux, number);

				} else if (primitive.isString()) {
					point.addField(keyInflux, strValue);
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

			influxDB = InfluxDBClientFactory.create(serverURL, token.toCharArray(), org, bucket);

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
	

	public InfluxDBClient getInfluxDB() {
		return influxDB;
	}

	public String getServerURL() {
		return serverURL;
	}

	public void setServerURL(String serverURL) {
		this.serverURL = serverURL;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getOrg() {
		return org;
	}

	public void setOrg(String org) {
		this.org = org;
	}

	public String getBucket() {
		return bucket;
	}

	public void setBucket(String bucket) {
		this.bucket = bucket;
	}
}
