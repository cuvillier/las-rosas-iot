package com.lasrosas.iot.ingestor.adapters.persisters.timeserie.mappers;

import com.lasrosas.iot.ingestor.adapters.persisters.timeserie.entities.TimeSerieTypeEntity;
import com.lasrosas.iot.ingestor.domain.model.timeserie.TimeSerieType;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface TimeSerieTypeEntityMapper {

    @Mapping(target="timeSeries", ignore = true)
    TimeSerieTypeEntity toTimeSerieTypeEntity(TimeSerieType domain);

    TimeSerieType toTimeSerieType(TimeSerieTypeEntity entity);
}
