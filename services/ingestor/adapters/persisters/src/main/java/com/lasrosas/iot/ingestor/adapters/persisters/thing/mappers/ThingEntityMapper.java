package com.lasrosas.iot.ingestor.adapters.persisters.thing.mappers;

import com.lasrosas.iot.ingestor.adapters.persisters.thing.entities.ThingEntity;
import com.lasrosas.iot.ingestor.domain.model.thing.Thing;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.SubclassExhaustiveStrategy;

@Mapper(componentModel = "spring", uses = {ThingGatewayEntityMapper.class, ThingProxyEntityMapper.class, ThingTypeEntityMapper.class}, subclassExhaustiveStrategy= SubclassExhaustiveStrategy.RUNTIME_EXCEPTION)
public interface ThingEntityMapper {

    @Mapping(target = "type", ignore = true)
    @Mapping(target = "gateway", ignore = true)
    @Mapping(target = "proxy", ignore = true)
    void update(Thing domain, @MappingTarget ThingEntity entity);

    Thing toThing(ThingEntity entity);
    ThingEntity toThingEntity(Thing domain);

    @Mapping(target = "type", ignore = true)
    @Mapping(target = "gateway", ignore = true)
    @Mapping(target = "proxy", ignore = true)
    ThingEntity toShallowThingEntity(Thing domain);
}
