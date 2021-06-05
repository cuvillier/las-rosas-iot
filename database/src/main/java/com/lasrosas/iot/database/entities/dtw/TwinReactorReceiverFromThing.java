package com.lasrosas.iot.database.entities.dtw;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.lasrosas.iot.database.entities.thg.Thing;

@Entity
@DiscriminatorValue(TwinReactorReceiverFromThing.DISCRIMINATOR)
public class TwinReactorReceiverFromThing extends TwinReactorReceiver {
	public static final String PREFIX = "rvt_";
	public static final String PREFIX_FK = PREFIX + "fk_";
	public static final String DISCRIMINATOR = "thg";

	public static final String COL_THING_FK = PREFIX_FK + Thing.PREFIX + "thing";
	public static final String PROP_THING = "thing";

	@ManyToOne
	@JoinColumn(name=COL_THING_FK)
	private Thing thing;

	public Thing getThing() {
		return thing;
	}

	public void setThing(Thing thing) {
		this.thing = thing;
	}
}
