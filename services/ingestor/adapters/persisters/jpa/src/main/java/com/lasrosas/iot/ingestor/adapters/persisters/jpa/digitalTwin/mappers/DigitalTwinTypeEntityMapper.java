package com.lasrosas.iot.ingestor.adapters.persisters.jpa.digitalTwin.mappers;

import com.lasrosas.iot.ingestor.adapters.persisters.jpa.digitalTwin.entities.DigitalTwinTypeEntity;
import com.lasrosas.iot.ingestor.adapters.persisters.jpa.digitalTwin.entities.twins.DynamicTwinTypeEntity;
import com.lasrosas.iot.ingestor.adapters.persisters.jpa.digitalTwin.entities.twins.FridgeTypeEntity;
import com.lasrosas.iot.ingestor.adapters.persisters.jpa.digitalTwin.entities.twins.MultiSwitchTypeEntity;
import com.lasrosas.iot.ingestor.adapters.persisters.jpa.digitalTwin.entities.twins.WaterTankTypeEntity;
import com.lasrosas.iot.ingestor.domain.model.digitalTwin.DigitalTwinType;
import com.lasrosas.iot.ingestor.domain.model.digitalTwin.twins.DynamicTwinType;
import com.lasrosas.iot.ingestor.domain.model.digitalTwin.twins.FridgeType;
import com.lasrosas.iot.ingestor.domain.model.digitalTwin.twins.MultiSwitchType;
import com.lasrosas.iot.ingestor.domain.model.digitalTwin.twins.WaterTankType;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.SubclassMapping;
import org.mapstruct.SubclassMappings;

@Mapper(componentModel = "spring", uses = {})
public interface DigitalTwinTypeEntityMapper {

    @SubclassMappings({
            @SubclassMapping(source= DynamicTwinTypeEntity.class, target= DynamicTwinType.class),
            @SubclassMapping(source= FridgeTypeEntity.class, target= FridgeType.class),
            @SubclassMapping(source= MultiSwitchTypeEntity.class, target= MultiSwitchType.class),
            @SubclassMapping(source= WaterTankTypeEntity.class, target= WaterTankType.class),
    })
    DigitalTwinType  toDigitalSpace(DigitalTwinTypeEntity entity);

    @SubclassMappings({
            @SubclassMapping(source= DynamicTwinType.class, target= DynamicTwinTypeEntity.class),
            @SubclassMapping(source= FridgeType.class, target= FridgeTypeEntity.class),
            @SubclassMapping(source= MultiSwitchType.class, target= MultiSwitchTypeEntity.class),
            @SubclassMapping(source= WaterTankType.class, target= WaterTankTypeEntity.class),
    })
    @Mapping(target="twins", ignore = true)
    DigitalTwinTypeEntity toDigitalSpaceEntity(DigitalTwinType domain);
}
