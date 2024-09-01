package com.lasrosas.iot.ingestor.adapters.persisters.jpa.digitalTwin.mappers;

import com.lasrosas.iot.ingestor.adapters.persisters.jpa.digitalTwin.entities.DigitalTwinEntity;
import com.lasrosas.iot.ingestor.adapters.persisters.jpa.digitalTwin.entities.twins.DynamicTwinEntity;
import com.lasrosas.iot.ingestor.adapters.persisters.jpa.digitalTwin.entities.twins.FridgeEntity;
import com.lasrosas.iot.ingestor.adapters.persisters.jpa.digitalTwin.entities.twins.MultiSwitchEntity;
import com.lasrosas.iot.ingestor.adapters.persisters.jpa.digitalTwin.entities.twins.WaterTankEntity;
import com.lasrosas.iot.ingestor.domain.model.digitalTwin.DigitalTwin;
import com.lasrosas.iot.ingestor.domain.model.digitalTwin.twins.DynamicTwin;
import com.lasrosas.iot.ingestor.domain.model.digitalTwin.twins.Fridge;
import com.lasrosas.iot.ingestor.domain.model.digitalTwin.twins.MultiSwitch;
import com.lasrosas.iot.ingestor.domain.model.digitalTwin.twins.WaterTank;
import org.mapstruct.Mapper;
import org.mapstruct.SubclassMapping;
import org.mapstruct.SubclassMappings;

@Mapper(componentModel = "spring", uses = {
        DigitalTwinTypeEntityMapper.class,
        DigitalSpaceEntityMapper.class,
        ReactorReceiverEntityMapper.class,
        ReactorReceiverTypeEntityMapper.class,
        ReactorTypeEntityMapper.class
})
public interface DigitalTwinEntityMapper {

    @SubclassMappings({
            @SubclassMapping(source= DynamicTwinEntity.class, target = DynamicTwin.class),
            @SubclassMapping(source= FridgeEntity.class, target = Fridge.class),
            @SubclassMapping(source= MultiSwitchEntity.class, target = MultiSwitch.class),
            @SubclassMapping(source= WaterTankEntity.class, target = WaterTank.class)
    })
    DigitalTwin toDigitalTwin(DigitalTwinEntity entity);

    @SubclassMappings({
            @SubclassMapping(source= DynamicTwin.class, target = DynamicTwinEntity.class),
            @SubclassMapping(source= Fridge.class, target = FridgeEntity.class),
            @SubclassMapping(source= MultiSwitch.class, target = MultiSwitchEntity.class),
            @SubclassMapping(source= WaterTank.class, target = WaterTankEntity.class)
    })
    DigitalTwinEntity toDigitalTwinEntity(DigitalTwin domain);
}
