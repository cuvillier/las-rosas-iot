package com.lasrosas.iot.ingestor.adapters.persisters.jpa.digitalTwin.mappers;

import com.lasrosas.iot.ingestor.adapters.persisters.jpa.digitalTwin.entities.DigitalSpaceEntity;
import com.lasrosas.iot.ingestor.domain.model.digitalTwin.DigitalSpace;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {})
public interface DigitalSpaceEntityMapper {
    DigitalSpace toDigitalSpace(DigitalSpaceEntity entity);
    DigitalSpaceEntity toDigitalSpaceEntity(DigitalSpace domain);
}
