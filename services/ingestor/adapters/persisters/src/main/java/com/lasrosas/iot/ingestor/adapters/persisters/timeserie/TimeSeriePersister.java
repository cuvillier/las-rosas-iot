package com.lasrosas.iot.ingestor.adapters.persisters.timeserie;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.lasrosas.iot.ingestor.adapters.persisters.thing.entities.ThingProxyEntity;
import com.lasrosas.iot.ingestor.adapters.persisters.thing.repositories.ThingEntityRepository;
import com.lasrosas.iot.ingestor.adapters.persisters.timeserie.entities.TimeSerieEntity;
import com.lasrosas.iot.ingestor.adapters.persisters.timeserie.entities.TimeSeriePointEntity;
import com.lasrosas.iot.ingestor.adapters.persisters.timeserie.entities.TimeSerieTypeEntity;
import com.lasrosas.iot.ingestor.adapters.persisters.timeserie.repositories.TimeSerieEntityRepository;
import com.lasrosas.iot.ingestor.adapters.persisters.timeserie.repositories.TimeSeriePointEntityRepository;
import com.lasrosas.iot.ingestor.adapters.persisters.timeserie.repositories.TimeSerieTypeEntityRepository;
import com.lasrosas.iot.ingestor.domain.message.EventMessage;
import com.lasrosas.iot.ingestor.domain.model.timeserie.TimeSeriePoint;
import com.lasrosas.iot.ingestor.domain.ports.stores.TimeSerieStore;
import com.lasrosas.iot.ingestor.shared.JsonUtils;
import jakarta.persistence.EntityManager;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import static org.hibernate.query.sqm.tree.SqmNode.log;

@Component
@AllArgsConstructor
@NoArgsConstructor
public class TimeSeriePersister implements TimeSerieStore {
    private ThingEntityRepository thingRepo;
    private TimeSerieTypeEntityRepository typeRepo;
    private TimeSerieEntityRepository timeSerieRepo;
    private TimeSeriePointEntityRepository pointRepo;
    private EntityManager em;
    private boolean storeProxyTime = false;

    public void updateProxy(TimeSeriePoint pointDomain) {
        var point = pointRepo.getReferenceById(pointDomain.getTechid());
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
        var changes = JsonUtils.mergeJsonObjects(newValue, subjson, storeProxyTime ? point.getTime() : null);
        if (changes != 0)
            proxy.setValues(JsonUtils.toJson(proxyValue));
    }

    private void insertPoint(EventMessage event) {
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

        timeSerie = timeSerieRepo.getByThingAndTypeAndSensor(thing, timeSerieType, sensor).orElseGet( () -> {

                var tsr = TimeSerieEntity.builder()
                        .thing(thing)
                        .type(timeSerieType)
                        .sensor(sensor)
                        .build();

                timeSerieRepo.save(tsr);
                return tsr;
        });

        // Create point or update it
        var time = message.getTime();
        String json = JsonUtils.toJson(message);
        var optPoint = pointRepo.getByTimeAndTimeSerie(time, timeSerie);

        if(optPoint.isPresent()) {
            log.warn(String.format("Duplicated point for %s ", timeSerie.getTechid() + " time= " + time + ". Skiped value=" + json));
            return;
        }

        var point = TimeSeriePointEntity.builder()
                .timeSerie(timeSerie)
                .time(time)
                .build();

        em.persist(point);

        point.setValue(json);
        timeSerie.setCurrentValue(point);
    }
}
