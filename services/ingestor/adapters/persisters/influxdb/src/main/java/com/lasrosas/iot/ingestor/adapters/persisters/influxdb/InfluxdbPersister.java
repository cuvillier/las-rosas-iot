package com.lasrosas.iot.ingestor.adapters.persisters.influxdb;

import com.influxdb.client.InfluxDBClient;
import com.influxdb.client.InfluxDBClientFactory;
import com.influxdb.client.WriteApi;
import com.influxdb.client.domain.WritePrecision;
import com.influxdb.client.write.Point;
import com.lasrosas.iot.ingestor.domain.message.EventMessage;
import com.lasrosas.iot.ingestor.domain.model.timeserie.TimeSerie;
import com.lasrosas.iot.ingestor.shared.JsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.lang.reflect.Modifier;
import java.sql.Timestamp;
import java.time.temporal.Temporal;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Component
@Slf4j
public class InfluxdbPersister {
    private InfluxDBClient influxDB;

    @Value("${ingestor.adapters.persisters.influxdb.bucket:\"http://127.0.0.1:8086\"}")
    private String url;

    @Value("${ingestor.adapters.persisters.influxdb.token}")
    private String token;

    @Value("${ingestor.adapters.persisters.influxdb.org:lasrosasiot}")
    private String organization;

    @Value("${ingestor.adapters.persisters.influxdb.bucket:lasrosasiot}")
    private String bucket;

    @Value("${ingestor.adapters.persisters.influxdb.dry:false}")
    private boolean dryMode = false;

    private Set<Class<?>> initedClasses = new HashSet<Class<?>>();

    private InfluxDBClient influxDB() {
        if (influxDB == null)
            influxDB = InfluxDBClientFactory.create("http://localhost:8086", token.toCharArray(), organization, bucket);

        return influxDB;
    }

    public void writePoint(TimeSerie timeSerie, EventMessage event) {

        var time = event.getMessage().getTime();
        var timestamp = Timestamp.valueOf(time).getTime();

        var measurment = timeSerie.getInfluxdbMeasurement();

        var influxdbPoint = Point.measurement(measurment).time(timestamp, WritePrecision.MS);

        var fields = new HashMap<String, Object>();
        addFields(fields, event.getMessage(), null);
        influxdbPoint.addFields(fields);

        var influxDB = influxDB();
        try (WriteApi writeApi = influxDB.getWriteApi()) {
            log.debug("Write Point to InfluxDB " + time.toString() + ", " + measurment + ",  " + JsonUtils.toJson(fields));
            writeApi.writePoint(influxdbPoint);
        }
    }

    void addFields(Map<String, Object> fields, Object values, String prefix) {
        try {

            for (Class<?> theClass = values.getClass(); theClass != Object.class; theClass = theClass.getSuperclass()) {

                boolean classInitited = initedClasses.contains(theClass);
                if (!classInitited) initedClasses.add(theClass);

                for (var field : theClass.getDeclaredFields()) {

                    // Skip static fields
                    if (Modifier.isStatic(field.getModifiers())) continue;

                    // Skip non persistent point data
                    //if (field.isAnnotationPresent(NotPartOfState.class)) continue;

                    field.setAccessible(true);

                    var fieldName = prefix == null ? field.getName() : prefix + "_" + field.getName();
                    var value = field.get(values);

                    if (value != null) {

                        if (Temporal.class.isAssignableFrom(field.getType()))
                            continue;

//                        if (field.isAnnotationPresent(PartOfState.class))
//                            addFields(fields, value, fieldName);
//                        else
                        {
                            if (field.getType().isEnum()) {
                                Enum<?> e = (Enum<?>) value;
                                fields.put(fieldName, e.ordinal());
                            } else if (!(value instanceof Character))
                                fields.put(fieldName, value);
                        }
                    }
                }
            }

        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
