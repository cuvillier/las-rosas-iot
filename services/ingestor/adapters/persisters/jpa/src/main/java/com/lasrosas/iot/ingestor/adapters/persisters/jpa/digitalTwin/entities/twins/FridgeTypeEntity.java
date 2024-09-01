package com.lasrosas.iot.ingestor.adapters.persisters.jpa.digitalTwin.entities.twins;

import com.lasrosas.iot.ingestor.adapters.persisters.jpa.digitalTwin.entities.DigitalTwinTypeEntity;
import jakarta.persistence.*;
import lombok.experimental.SuperBuilder;

@Entity
@DiscriminatorValue(FridgeTypeEntity.DISCRIMINATOR)
@SuperBuilder
public class FridgeTypeEntity extends DigitalTwinTypeEntity {
	public static final String DISCRIMINATOR = "fri";

}