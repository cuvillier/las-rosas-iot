package com.lasrosas.iot.ingestor.adapters.persisters.jpa.digitalTwin.mappers;

import com.lasrosas.iot.ingestor.adapters.persisters.jpa.digitalTwin.entities.ReactorReceiverTypeEntity;
import com.lasrosas.iot.ingestor.domain.model.digitalTwin.ReactorReceiver;
import com.lasrosas.iot.ingestor.domain.model.digitalTwin.ReactorReceiverType;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {})
public interface ReactorReceiverTypeEntityMapper {
    ReactorReceiverType toReactorReceiverType(ReactorReceiverTypeEntity entity);
    ReactorReceiverTypeEntity toReactorReceiverTypeEntity(ReactorReceiverType domain);
}
