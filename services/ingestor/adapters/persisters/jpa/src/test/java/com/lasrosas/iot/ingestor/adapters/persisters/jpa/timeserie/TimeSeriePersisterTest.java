package com.lasrosas.iot.ingestor.adapters.persisters.jpa.timeserie;

import com.lasrosas.iot.ingestor.adapters.persisters.jpa.thing.entities.ThingEntity;
import com.lasrosas.iot.ingestor.adapters.persisters.jpa.timeserie.entities.TimeSerieEntity;
import com.lasrosas.iot.ingestor.adapters.persisters.jpa.timeserie.entities.TimeSeriePointEntity;
import com.lasrosas.iot.ingestor.adapters.persisters.jpa.timeserie.entities.TimeSerieTypeEntity;
import com.lasrosas.iot.ingestor.adapters.persisters.jpa.timeserie.mappers.TimeSeriePointEntityMapper;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(MockitoExtension.class)
public class TimeSeriePersisterTest {

    @Mock
    private EntityManager em;

    @InjectMocks
    private TimeSeriePersisterJPA persister;

    @Autowired
    private TimeSeriePointEntityMapper mapper;

    @Test
    public void updateProxy() {

        // Given
        var value = "{\"temperature\": 12.3, \"humidity\": 80}";

        var thingEntity = ThingEntity.builder()
                .build();

        var timeSerieTypeEntity = TimeSerieTypeEntity.builder()
                .schema("AirEnvironment")
                .build();

        var timeSerieEntity = TimeSerieEntity.builder()
                .thing(thingEntity)
                .type(timeSerieTypeEntity)
                .build();

        var pointEntity = TimeSeriePointEntity.builder()
                .techid(123L)
                .time(LocalDateTime.now())
                .value(value)
                .timeSerie(timeSerieEntity)
                .build();

        // When
        persister.updateProxy(pointEntity);

        // Then
        assertNotNull(thingEntity.getProxy());
        assertEquals("{\"AirEnvironment\":{\"temperature\":12.3,\"humidity\":80}}", thingEntity.getProxy().getValues());
    }
}
