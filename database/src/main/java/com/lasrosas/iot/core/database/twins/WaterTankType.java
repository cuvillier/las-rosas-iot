package com.lasrosas.iot.core.database.twins;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import com.lasrosas.iot.core.database.entities.dtw.DigitalTwinType;

@Entity
@DiscriminatorValue(WaterTankType.DISCRIMINATOR)
public class WaterTankType extends DigitalTwinType {
	public static final String DISCRIMINATOR = "wat";

}