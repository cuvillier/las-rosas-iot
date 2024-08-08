package com.lasrosas.iot.ingestor.adapters.persisters.timeserie.mappers;

import com.lasrosas.iot.ingestor.adapters.persisters.timeserie.entities.TimeSerieEntity;
import com.lasrosas.iot.ingestor.domain.model.timeserie.TimeSerie;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.SubclassExhaustiveStrategy;

@Mapper(componentModel = "spring", uses = {TimeSerieTypeEntityMapper.class, TimeSeriePointEntityMapper.class}, subclassExhaustiveStrategy= SubclassExhaustiveStrategy.RUNTIME_EXCEPTION)
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
