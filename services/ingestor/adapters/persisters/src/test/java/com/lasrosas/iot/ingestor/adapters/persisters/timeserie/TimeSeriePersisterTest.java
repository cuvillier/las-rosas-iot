package com.lasrosas.iot.ingestor.adapters.persisters.timeserie;

import com.lasrosas.iot.ingestor.adapters.persisters.thing.entities.ThingEntity;
import com.lasrosas.iot.ingestor.adapters.persisters.timeserie.entities.TimeSerieEntity;
import com.lasrosas.iot.ingestor.adapters.persisters.timeserie.entities.TimeSeriePointEntity;
import com.lasrosas.iot.ingestor.adapters.persisters.timeserie.entities.TimeSerieTypeEntity;
import com.lasrosas.iot.ingestor.adapters.persisters.timeserie.repositories.TimeSeriePointEntityRepository;
import com.lasrosas.iot.ingestor.domain.model.thing.Thing;
import com.lasrosas.iot.ingestor.domain.model.timeserie.TimeSeriePoint;
import com.lasrosas.iot.ingestor.shared.JsonUtils;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(MockitoExtension.class)
public class TimeSeriePersisterTest {

    @Mock
    private TimeSeriePointEntityRepository timeserieRepo;

    @Mock
    private EntityManager em;

    @InjectMocks
    private TimeSeriePersister persister;

    @Test
    public void updateProxy() {

        // Given
        var value = "{\"temperature\": 12.3, \"humidity\": 80}";
        var point = TimeSeriePoint.builder()
                        .techid(123L)
                        .time(LocalDateTime.now())
                        .value(value)
                        .build();

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
        Mockito.when(timeserieRepo.getReferenceById(123L)).thenReturn(pointEntity);
        persister.updateProxy(point);

        // Then
        assertNotNull(thingEntity.getProxy());
        assertEquals("{\"AirEnvironment\":{\"temperature\":12.3,\"humidity\":80}}", thingEntity.getProxy().getValues());
    }
}
