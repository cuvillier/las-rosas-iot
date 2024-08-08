package com.lasrosas.iot.ingestor.adapters.persisters.jpa.timeserie.mappers;

import com.lasrosas.iot.ingestor.adapters.persisters.jpa.timeserie.entities.TimeSeriePointEntity;
import com.lasrosas.iot.ingestor.domain.model.timeserie.TimeSeriePoint;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface TimeSeriePointEntityMapper {

    @Mapping(target="timeSerie", ignore = true)
    TimeSeriePointEntity toTimeSeriePointEntity(TimeSeriePoint domain);

    TimeSeriePoint toTimeSeriePoint(TimeSeriePointEntity entity);
}
