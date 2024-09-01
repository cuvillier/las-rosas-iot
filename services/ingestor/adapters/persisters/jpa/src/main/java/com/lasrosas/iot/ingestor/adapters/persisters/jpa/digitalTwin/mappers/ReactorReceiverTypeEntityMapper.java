package com.lasrosas.iot.ingestor.adapters.persisters.jpa.digitalTwin.mappers;

import com.lasrosas.iot.ingestor.adapters.persisters.jpa.digitalTwin.entities.ReactorReceiverTypeEntity;
import com.lasrosas.iot.ingestor.domain.model.digitalTwin.ReactorReceiver;
import com.lasrosas.iot.ingestor.domain.model.digitalTwin.ReactorReceiverType;
import org.mapstruct.*;

@Mapper(componentModel = "spring", uses = {ReactorReceiverEntityMapper.class})
public interface ReactorReceiverTypeEntityMapper {
    ReactorReceiverType toReactorReceiverType(ReactorReceiverTypeEntity entity);

    @Mapping(target="receivers", ignore = true)
    ReactorReceiverTypeEntity toReactorReceiverTypeEntity(ReactorReceiverType domain);
}
