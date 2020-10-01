package com.lasrosas.iot.database.entities.dtw;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue(ThingTwinType.DISCRIMINATOR)
public class ThingTwinType extends DigitalTwinType {
	public static final String DISCRIMINATOR = "THG";

}
