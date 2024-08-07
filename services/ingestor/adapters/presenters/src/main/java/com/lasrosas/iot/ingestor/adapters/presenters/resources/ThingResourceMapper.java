package com.lasrosas.iot.ingestor.adapters.presenters.resources;

import com.lasrosas.iot.ingestor.domain.model.thing.Thing;
import org.mapstruct.Mapper;
import org.mapstruct.SubclassExhaustiveStrategy;
import org.mapstruct.SubclassMapping;
import org.mapstruct.factory.Mappers;

@Mapper(subclassExhaustiveStrategy = SubclassExhaustiveStrategy.RUNTIME_EXCEPTION)
public interface ThingResourceMapper {
    ThingResourceMapper MAPPER = Mappers.getMapper(ThingResourceMapper.class);

    Thing toThing(ThingResource resource);
    ThingResource toThingResource(Thing domain);
}
