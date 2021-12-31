package com.lasrosas.iot.core.ingestor.timeSerieWriter.impl;

import java.lang.reflect.Modifier;
import java.time.temporal.Temporal;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.Message;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.influxdb.LogLevel;
import com.influxdb.client.InfluxDBClient;
import com.influxdb.client.InfluxDBClientFactory;
import com.influxdb.client.WriteApi;
import com.influxdb.client.domain.WritePrecision;
import com.influxdb.client.write.Point;
import com.lasrosas.iot.core.database.entities.tsr.TimeSerie;
import com.lasrosas.iot.core.ingestor.timeSerieWriter.api.InfluxDBConfig;
import com.lasrosas.iot.core.ingestor.timeSerieWriter.api.WriteInfluxDB;
import com.lasrosas.iot.core.shared.telemetry.NotPartOfState;
import com.lasrosas.iot.core.shared.telemetry.PartOfState;
import com.lasrosas.iot.core.shared.utils.LasRosasHeaders;
import com.lasrosas.iot.core.shared.utils.TimeUtils;

public class WriteInfluxDBImpl implements WriteInfluxDB {
	public static final Logger log = LoggerFactory.getLogger(WriteInfluxDBImpl.class);

	private InfluxDBConfig config;
	private InfluxDBClient influxDB;
	private boolean dryMode = false;
	private Set<Class<?>> initedClasses = new HashSet<Class<?>>();
	private Gson gson = new GsonBuilder().create();

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

	@Override
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void writePoint(TimeSerie tsr, Message<?>  imessage) {

		var time = LasRosasHeaders.time(imessage);
		var timestamp = TimeUtils.timestamp(time);

		var measurment = tsr.getInfluxdbMeasurement();

		if( measurment == null) {

			String naturalId;
			if(	LasRosasHeaders.twinNaturalId(imessage).isPresent() )
				naturalId = "TWI_"+LasRosasHeaders.twinNaturalId(imessage).get();
			else
				naturalId = "THG_"+LasRosasHeaders.thingNaturalId(imessage).get();

			var payloadTypeName = imessage.getPayload().getClass().getSimpleName();

			measurment = (naturalId + "_" + payloadTypeName).replaceAll("\\.", "_");
			tsr.setInfluxdbMeasurement(measurment);
		}

		var sensor = tsr.getSensor();

		if( sensor != null )
			measurment += "_" + sensor;

		var influxdbPoint = Point.measurement(measurment).time(timestamp, WritePrecision.MS);

		var fields = new HashMap<String, Object>();
		addFields(fields, imessage.getPayload(), null);
		influxdbPoint.addFields(fields);

		var influxDB = influxDB();
		try (WriteApi writeApi = influxDB.getWriteApi()) {
			log.debug("Write Point to InfluxDB " + time.toString() + ", " + measurment + ",  " + gson.toJson(fields));
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
					if(field.isAnnotationPresent(NotPartOfState.class)) continue;

					field.setAccessible(true);

					var fieldName = prefix == null ? field.getName(): prefix + "_" + field.getName();
					var value = field.get(values);

					if(value != null) {

						if(Temporal.class.isAssignableFrom(field.getType()))
							continue;

						if(field.isAnnotationPresent(PartOfState.class))
							addFields(fields, value, fieldName);
						else {
							if( field.getType().isEnum() ) {
								Enum<?> e = (Enum<?>)value;
								fields.put(fieldName, e.ordinal());
							} else
								if(!(value instanceof Character)) fields.put(fieldName, value);
						}
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
