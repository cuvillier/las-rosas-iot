package com.lasrosas.iot.ingestor.adapters.persisters.jpa.digitalTwin.mappers;

import com.lasrosas.iot.ingestor.adapters.persisters.jpa.digitalTwin.entities.ReactorReceiverEntity;
import com.lasrosas.iot.ingestor.domain.model.digitalTwin.ReactorReceiver;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring", uses = {
        DigitalSpaceEntityMapper.class,
        DigitalTwinTypeEntityMapper.class,
        DigitalTwinEntityMapper.class,
        ReactorReceiverTypeEntityMapper.class,
})
public interface ReactorReceiverEntityMapper {

    @Mappings({
            @Mapping(source="sourceThing.techid", target = "sourceThingId"),
            @Mapping(source="sourceTwin.techid", target = "sourceTwinId"),
            @Mapping(source="targetTwin.techid", target = "targetTwinId")
    })
    ReactorReceiver toDigitalSpace(ReactorReceiverEntity entity);

    @Mappings({
            @Mapping(target="sourceThing", ignore = true),
            @Mapping(target="sourceTwin", ignore = true),
            @Mapping(target="targetTwin", ignore = true)
    })
    ReactorReceiverEntity toDigitalSpaceEntity(ReactorReceiver domain);
}
