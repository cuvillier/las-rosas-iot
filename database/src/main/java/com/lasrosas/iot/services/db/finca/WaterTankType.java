package com.lasrosas.iot.services.db.finca;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import com.lasrosas.iot.services.db.entities.dtw.DigitalTwinType;

@Entity
@DiscriminatorValue(WaterTankType.DISCRIMINATOR)
public class WaterTankType extends DigitalTwinType {
	public static final String DISCRIMINATOR = "WAT";

}