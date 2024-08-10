package com.lasrosas.iot.ingestor.adapters.persisters.jpa.timeserie.mappers;

import com.lasrosas.iot.ingestor.adapters.persisters.jpa.thing.mappers.ThingGatewayEntityMapper;
import com.lasrosas.iot.ingestor.adapters.persisters.jpa.thing.mappers.ThingProxyEntityMapper;
import com.lasrosas.iot.ingestor.adapters.persisters.jpa.thing.mappers.ThingTypeEntityMapper;
import com.lasrosas.iot.ingestor.adapters.persisters.jpa.timeserie.entities.TimeSerieEntity;
import com.lasrosas.iot.ingestor.domain.model.thing.ThingGateway;
import com.lasrosas.iot.ingestor.domain.model.timeserie.TimeSerie;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.SubclassExhaustiveStrategy;

@Mapper(componentModel = "spring", uses = {ThingGatewayEntityMapper.class, ThingTypeEntityMapper.class, ThingProxyEntityMapper.class, TimeSerieTypeEntityMapper.class, TimeSeriePointEntityMapper.class})
public interface TimeSerieEntityMapper {

    @Mapping(target = "type", ignore = true)
    @Mapping(target = "points", ignore = true)
    void update(TimeSerie domain, @MappingTarget TimeSerieEntity entity);

    TimeSerie toTimeSerie(TimeSerieEntity entity);

    @Mapping(target = "points", ignore = true)
    TimeSerieEntity toTimeSerieEntity(TimeSerie domain);

    @Mapping(target = "type", ignore = true)
    @Mapping(target = "thing", ignore = true)
    @Mapping(target = "points", ignore = true)
    TimeSerieEntity toShallowTimeSerieEntity(TimeSerie domain);
}
