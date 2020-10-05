package com.lasrosas.iot.database.entities.dtw;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue(ThingTwin.DISCRIMINATOR)
public class ThingTwin extends DigitalTwin {
	public static final String PREFIX = "YTW_";
	public static final String DISCRIMINATOR = "THG";
}
