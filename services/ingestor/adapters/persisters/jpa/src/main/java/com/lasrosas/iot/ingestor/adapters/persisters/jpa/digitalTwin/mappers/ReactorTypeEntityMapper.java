package com.lasrosas.iot.ingestor.adapters.persisters.jpa.digitalTwin.mappers;

import com.lasrosas.iot.ingestor.adapters.persisters.jpa.digitalTwin.entities.ReactorTypeEntity;
import com.lasrosas.iot.ingestor.domain.model.digitalTwin.ReactorType;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {})
public interface ReactorTypeEntityMapper {
    ReactorType toReactorReceiverType(ReactorTypeEntity entity);
    ReactorTypeEntity toReactorReceiverTypeEntity(ReactorType domain);
}
