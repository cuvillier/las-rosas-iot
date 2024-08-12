package com.lasrosas.iot.ingestor.adapters.persisters.jpa.timeserie;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.lasrosas.iot.ingestor.adapters.persisters.jpa.timeserie.repositories.TimeSeriePointEntityRepository;
import com.lasrosas.iot.ingestor.adapters.persisters.jpa.thing.entities.ThingProxyEntity;
import com.lasrosas.iot.ingestor.adapters.persisters.jpa.thing.repositories.ThingEntityRepository;
import com.lasrosas.iot.ingestor.adapters.persisters.jpa.timeserie.entities.TimeSerieEntity;
import com.lasrosas.iot.ingestor.adapters.persisters.jpa.timeserie.entities.TimeSeriePointEntity;
import com.lasrosas.iot.ingestor.adapters.persisters.jpa.timeserie.entities.TimeSerieTypeEntity;
import com.lasrosas.iot.ingestor.adapters.persisters.jpa.timeserie.repositories.TimeSerieEntityRepository;
import com.lasrosas.iot.ingestor.adapters.persisters.jpa.timeserie.repositories.TimeSerieTypeEntityRepository;
import com.lasrosas.iot.ingestor.domain.message.ThingEventMessage;
import com.lasrosas.iot.ingestor.domain.model.thing.ThingType;
import com.lasrosas.iot.ingestor.domain.ports.stores.ThingTypeSchema;
import com.lasrosas.iot.ingestor.domain.ports.stores.TimeSerieStore;
import com.lasrosas.iot.ingestor.shared.JsonUtils;
import jakarta.persistence.EntityManager;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;

import java.util.List;

import static org.hibernate.query.sqm.tree.SqmNode.log;

@Component("jpaTimeSerieStore")
@Transactional
@AllArgsConstructor
public class TimeSeriePersisterJPA implements TimeSerieStore {
    private ThingEntityRepository thingRepo;
    private TimeSerieTypeEntityRepository typeRepo;
    private TimeSerieEntityRepository timeSerieRepo;
    private TimeSeriePointEntityRepository pointRepo;
    private EntityManager em;

    protected void updateProxy(TimeSeriePointEntity point) {
        var thing = point.getTimeSerie().getThing();

        // Twin point
        if (thing == null) return;

        var proxy = thing.getProxy();

        if(proxy == null) {
            proxy = ThingProxyEntity.builder().thing(thing).build();
            thing.setProxy(proxy);
            em.persist(proxy);
        }

        // Merge the proxy values

        var proxyValue = proxy.getValuesAsObjectNode();
        if (proxyValue == null)
            proxyValue = JsonUtils.objectNode();

        var schema = point.getTimeSerie().getType().getSchema();
        var sensor = point.getTimeSerie().getSensor();

        var subjsonName = sensor == null ? schema : sensor + "_" + schema;
        var subjson = (ObjectNode)proxyValue.get(subjsonName);
        if (subjson == null) {
            subjson = JsonUtils.objectNode();
            proxyValue.putIfAbsent(subjsonName, subjson);
        }

        var newValue = point.getValueAsObjectNode();
        var changes = JsonUtils.mergeJsonObjects(newValue, subjson, false ? point.getTime() : null);
        if (changes != 0) {
            proxy.setValues(JsonUtils.toJson(proxyValue));
        }
    }

    public void insertPoint(ThingEventMessage event) {
        try {
            var message = event.getMessage();
            var schema = message.getSchema();

            // Get the TimeSerieType, or create it the first time
            TimeSerieTypeEntity timeSerieType = typeRepo.getBySchema(schema).orElseGet(() -> {

                var newTst = TimeSerieTypeEntity.builder()
                        .schema(schema)
                        .build();

                typeRepo.save(newTst);
                return newTst;
            });

            // Get TimeSerie or create it the first time
            TimeSerieEntity timeSerie;
/*
        var twinId = event.getDigitalTwinId();
        if( twinId != null) {
            var twin = twinRepo.findById(twinId).get();
            tsr = tsrRepo.findByTwinAndType(twin, tst);
            if (tsr == null) {
                tsr = new TimeSerie(twin, tst);
                tsrRepo.save(tsr);
            }
        } else {
*/
            var sensor = message.getSensor();

            var thingId = event.getThingId();
            var thing = thingRepo.findById(thingId).orElseThrow();

            timeSerie = timeSerieRepo.getByThingAndTypeAndSensor(thing, timeSerieType, sensor).orElseGet(() -> {

                var tsr = TimeSerieEntity.builder()
                        .thing(thing)
                        .type(timeSerieType)
                        .sensor(sensor)
                        .influxdbMeasurement(event.getMeasurement())
                        .build();

                timeSerieRepo.save(tsr);
                return tsr;
            });

            // Create point or update it
            var time = message.getTime();

            // Remove the fields already written in the TimeSerie and TimeSerieType
            var objectNode = JsonUtils.toObjectNode(event.getMessage());
            objectNode.remove("time");
            objectNode.remove("schema");
            objectNode.remove("correlationId");
            objectNode.remove("sensor");
            var json = JsonUtils.toJson(objectNode);

            var optPoint = pointRepo.getByTimeAndTimeSerie(time, timeSerie);

            if (optPoint.isPresent()) {
                log.warn(String.format("Duplicated point for %s ", timeSerie.getTechid() + " time= " + time + ". Skiped value=" + json));
                return;
            }

            var point = TimeSeriePointEntity.builder()
                    .timeSerie(timeSerie)
                    .time(time)
                    .correlationId(message.getCorrelationId())
                    .build();

            em.persist(point);

            point.setValue(json);
            timeSerie.setCurrentValue(point);
            updateProxy(point);
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
