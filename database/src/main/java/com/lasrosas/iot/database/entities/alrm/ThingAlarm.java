package com.lasrosas.iot.database.entities.alrm;

import java.time.LocalDateTime;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

import com.lasrosas.iot.database.entities.dtw.ThingTwin;
import com.lasrosas.iot.database.entities.thg.Thing;

@Entity
@DiscriminatorValue(ThingTwin.DISCRIMINATOR)
public class ThingAlarm extends Alarm {

	public static final String PREFIX = "TAL_";
	public static final String PREFIX_FK = PREFIX + "FK_";

	public static final String DISCRIMINATOR = "THG";
	public static final String COL_THING_FK = PREFIX_FK + Thing.PREFIX + "THING";
	public static final String PROP_THING = "thing";

	@OneToOne
	@JoinColumn(name=COL_THING_FK)
	private Thing thing;

	public ThingAlarm() {
		super();
	}

	public ThingAlarm(Thing thing, AlarmType type, LocalDateTime time) {
		super(type, time);
		this.thing = thing;
	}
	
	public Thing getThing() {
		return thing;
	}

	public void setThing(Thing thing) {
		this.thing = thing;
	}
}
