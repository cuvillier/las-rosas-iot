package com.lasrosas.iot.core.database.entities.dtw;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.springframework.messaging.support.MessageBuilder;

import com.lasrosas.iot.core.database.entities.thg.Thing;
import com.lasrosas.iot.core.shared.telemetry.Order;
import com.lasrosas.iot.core.shared.utils.LasRosasHeaders;

@Entity
@DiscriminatorValue(TwinReactorReceiverFromThing.DISCRIMINATOR)
public class TwinReactorReceiverFromThing extends TwinReactorReceiver {
	public static final String PREFIX = "rvt_";
	public static final String PREFIX_FK = PREFIX + "fk_";
	public static final String DISCRIMINATOR = "thg";

	public static final String COL_THING_SENSOR = Thing.PREFIX + "thing_sensor";
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

	@Override
	public void addOrderHeaders(MessageBuilder<Order> builder) {
		builder
			.setHeader(LasRosasHeaders.THING_ID, thing.getTechid())
			.setHeader(LasRosasHeaders.THING_NATURAL_ID, thing.getNaturalId());			
	}
}
