package com.lasrosas.iot.services.db.entities.dtw;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import com.lasrosas.iot.services.db.entities.thg.ThingLora;

@Entity
@DiscriminatorValue(DynamicTwin.DISCRIMINATOR)
public class DynamicTwin extends DigitalTwin {
	public static final String PREFIX = "YTW_";
	public static final String DISCRIMINATOR = "DYN";
}
