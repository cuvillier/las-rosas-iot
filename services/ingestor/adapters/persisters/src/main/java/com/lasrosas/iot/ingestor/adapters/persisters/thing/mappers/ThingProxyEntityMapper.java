package com.lasrosas.iot.ingestor.adapters.persisters.thing.mappers;

import com.lasrosas.iot.ingestor.adapters.persisters.thing.entities.ThingProxyEntity;
import com.lasrosas.iot.ingestor.domain.model.thing.ThingProxy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ThingProxyEntityMapper {
    @Mapping(target="thing", ignore = true)
    ThingProxyEntity toThingProxyEntity(ThingProxy domain);

    ThingProxy toThingProxy(ThingProxyEntity entity);
}
