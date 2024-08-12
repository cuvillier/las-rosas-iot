package com.lasrosas.iot.ingestor.adapters.persisters.jpa.digitalTwin.mappers;

import com.lasrosas.iot.ingestor.adapters.persisters.jpa.digitalTwin.entities.DigitalTwinEntity;
import com.lasrosas.iot.ingestor.domain.model.digitalTwin.DigitalTwin;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {})
public interface DigitalTwinEntityMapper {
    DigitalTwin toDigitalSpace(DigitalTwinEntity entity);
    DigitalTwinEntity toDigitalSpaceEntity(DigitalTwin domain);
}
