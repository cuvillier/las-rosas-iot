package com.lasrosas.iot.ingestor.adapters.persisters.jpa.digitalTwin.mappers;

import com.lasrosas.iot.ingestor.adapters.persisters.jpa.digitalTwin.entities.DigitalTwinTypeEntity;
import com.lasrosas.iot.ingestor.domain.model.digitalTwin.DigitalTwinType;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {})
public interface DigitalTwinTypeEntityMapper {
    DigitalTwinType toDigitalSpace(DigitalTwinTypeEntity entity);
    DigitalTwinTypeEntity toDigitalSpaceEntity(DigitalTwinType domain);
}
