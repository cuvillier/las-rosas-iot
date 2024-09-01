package com.lasrosas.iot.ingestor.adapters.persisters.jpa.digitalTwin.entities.twins;

import com.lasrosas.iot.ingestor.adapters.persisters.jpa.digitalTwin.entities.DigitalTwinTypeEntity;
import jakarta.persistence.*;
import lombok.experimental.SuperBuilder;

@Entity
@DiscriminatorValue(WaterTankTypeEntity.DISCRIMINATOR)
@SuperBuilder
public class WaterTankTypeEntity extends DigitalTwinTypeEntity {
	public static final String DISCRIMINATOR = "wat";

}