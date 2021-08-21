package com.lasrosas.iot.ingestor.services.timeSerieWriter.impl;

import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.influxdb.LogLevel;
import com.influxdb.client.InfluxDBClient;
import com.influxdb.client.InfluxDBClientFactory;
import com.influxdb.client.WriteApi;
import com.influxdb.client.domain.WritePrecision;
import com.influxdb.client.write.Point;
import com.lasrosas.iot.influxdb.InfluxDBConfig;
import com.lasrosas.iot.ingestor.services.timeSerieWriter.api.WriteInfluxDB;
import com.lasrosas.iot.shared.telemetry.NotPartOfTelemetry;
import com.lasrosas.iot.shared.telemetry.Telemetry;
import com.lasrosas.iot.shared.telemetry.TelemetryState;
import com.lasrosas.iot.shared.utils.TimeUtils;

public class WriteInfluxDBImpl implements WriteInfluxDB {
	public static final Logger log = LoggerFactory.getLogger(WriteInfluxDBImpl.class);

	private InfluxDBConfig config;
	private InfluxDBClient influxDB;
	private boolean dryMode = false;
	private Set<Class<?>> initedClasses = new HashSet<Class<?>>();

	public WriteInfluxDBImpl(InfluxDBConfig config) {
		this.config = config;
	}

	public boolean isDryMode() {
		return dryMode;
	}

	public void setDryMode(boolean dryMode) {
		this.dryMode = dryMode;
	}

	private InfluxDBClient influxDB() {

		if(influxDB == null) {
			influxDB = InfluxDBClientFactory.create(
					config.getUrl(),
					config.getToken().toCharArray(),
					config.getOrg(),
					config.getBucket());
			if( config.isGzip()) influxDB.enableGzip();
			influxDB.setLogLevel(LogLevel.valueOf(config.getLogLevel()));
		}

		return influxDB;
	}

	public void writePoint(String measurement, Telemetry telemetry) {
		var timestamp = TimeUtils.timestamp(telemetry.getTime());
		var influxdbPoint = Point.measurement(measurement).time(timestamp, WritePrecision.MS);

		var fields = new HashMap<String, Object>();
		addFields(fields, telemetry, null);
		influxdbPoint.addFields(fields);

		var influxDB = influxDB();
		try (WriteApi writeApi = influxDB.getWriteApi()) {
			writeApi.writePoint(influxdbPoint);
		}
	}

	void addFields(Map<String, Object> fields, Object values, String prefix) {
		try {

			for(Class<?> theClass = values.getClass(); theClass != Object.class; theClass = theClass.getSuperclass()) {

				boolean classInitited = initedClasses.contains(theClass);
				if(!classInitited) initedClasses.add(theClass);

				for(var field: theClass.getDeclaredFields()) {

					// Skip static fields
					if(Modifier.isStatic(field.getModifiers())) continue;

					// Skip non persistent point data
					if(field.isAnnotationPresent(NotPartOfTelemetry.class)) continue;

					if(!classInitited) field.setAccessible((true));

					var fieldName = prefix == null ? field.getName(): prefix + "_" + field.getName();
					var value = field.get(values);

					if(value != null) {
						if(field.isAnnotationPresent(TelemetryState.class))
							addFields(fields, value, fieldName);
						else
							if(!(value instanceof Character)) fields.put(fieldName, value);
					}
				}
			}

		} catch(RuntimeException e) {
			throw e;
		} catch(Exception e) {
			throw new RuntimeException(e);
		}
	}
}
