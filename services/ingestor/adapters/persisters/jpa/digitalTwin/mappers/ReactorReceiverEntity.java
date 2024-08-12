package com.lasrosas.iot.ingestor.adapters.persisters.jpa.digitalTwin.mappers;

import com.lasrosas.iot.ingestor.domain.model.digitalTwin.ReactorReceiver;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {})
public interface ReactorReceiverEntity {
    ReactorReceiver toDigitalSpace(ReactorReceiverEntity entity);
    ReactorReceiverEntity toDigitalSpaceEntity(ReactorReceiver domain);
}
