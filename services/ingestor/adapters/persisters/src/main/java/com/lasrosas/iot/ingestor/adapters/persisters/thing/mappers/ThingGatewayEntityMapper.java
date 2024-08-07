package com.lasrosas.iot.ingestor.adapters.persisters.thing.mappers;

import com.lasrosas.iot.ingestor.adapters.persisters.thing.entities.ThingGatewayEntity;
import com.lasrosas.iot.ingestor.domain.model.thing.ThingGateway;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ThingGatewayEntityMapper {

    @Mapping(target="things", ignore = true)
    ThingGatewayEntity toThingGatewayEntity(ThingGateway domain);

    ThingGateway toThingGateway(ThingGatewayEntity entity);
}
