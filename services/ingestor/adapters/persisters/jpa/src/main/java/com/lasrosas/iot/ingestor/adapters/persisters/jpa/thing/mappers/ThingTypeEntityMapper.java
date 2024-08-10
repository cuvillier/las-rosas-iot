package com.lasrosas.iot.ingestor.adapters.persisters.jpa.thing.mappers;

import com.lasrosas.iot.ingestor.adapters.persisters.jpa.thing.entities.ThingTypeEntity;
import com.lasrosas.iot.ingestor.domain.model.thing.ThingType;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ThingTypeEntityMapper {

    @Mapping(target="things", ignore = true)
    ThingTypeEntity toThingTypeEntity(ThingType domain);

    ThingType toThingType(ThingTypeEntity entity);
}
