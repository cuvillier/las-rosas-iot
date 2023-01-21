package com.lasrosas.iot.alarm.database.entity;

import java.time.LocalDateTime;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.lasrosas.iot.core.database.entities.thg.Thing;

@Entity
@DiscriminatorValue(AlarmThing.DISCRIMINATOR)
public class AlarmThing extends Alarm {
	public static final String PREFIX = "ath_";
	public static final String DISCRIMINATOR = "thg";
	public static final String PREFIX_FK = PREFIX + "fk_";
	public static final String COL_THING_FK = PREFIX_FK + Thing.PREFIX + "thing";

	@ManyToOne
	@JoinColumn(name=COL_THING_FK)
	private Thing thing;

	public AlarmThing() {
	}

	public AlarmThing(Thing thing, LocalDateTime time, String type, String message, AlarmGravity gravity) {
		super(time, type, message, gravity);
		this.thing = thing;
	}

	public Thing getThing() {
		return thing;
	}

	public void setThing(Thing thing) {
		this.thing = thing;
	}

	@Override
	public String getSourceNaturalId() {
		return thing.getNaturalId();
	}
}
