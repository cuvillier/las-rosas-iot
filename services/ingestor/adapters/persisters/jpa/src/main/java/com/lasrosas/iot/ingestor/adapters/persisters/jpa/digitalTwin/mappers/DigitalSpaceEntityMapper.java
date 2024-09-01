package com.lasrosas.iot.ingestor.adapters.persisters.jpa.digitalTwin.mappers;

import com.lasrosas.iot.ingestor.adapters.persisters.jpa.digitalTwin.entities.DigitalSpaceEntity;
import com.lasrosas.iot.ingestor.domain.model.digitalTwin.DigitalSpace;
import org.mapstruct.MapMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {DigitalTwinTypeEntityMapper.class})
public interface DigitalSpaceEntityMapper {
    DigitalSpace toDigitalSpace(DigitalSpaceEntity entity);

    @Mapping(target = "digitalTwinTypes", ignore = true)
    DigitalSpaceEntity toDigitalSpaceEntity(DigitalSpace domain);
}
