package com.lasrosas.iot.database.finca;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import com.lasrosas.iot.database.entities.dtw.DigitalTwinType;

@Entity
@DiscriminatorValue(WaterTankType.DISCRIMINATOR)
public class WaterTankType extends DigitalTwinType {
	public static final String DISCRIMINATOR = "WAT";

}