package com.lasrosas.iot.alarm.database.entity;

import java.time.LocalDateTime;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.lasrosas.iot.core.database.entities.dtw.DigitalTwin;

@Entity
@DiscriminatorValue(AlarmTwin.DISCRIMINATOR)
public class AlarmTwin extends Alarm {
	public static final String PREFIX = "atw_";
	public static final String DISCRIMINATOR = "twi";
	public static final String PREFIX_FK = PREFIX + "fk_";
	public static final String COL_DIGITAL_TWIN_FK = PREFIX_FK + DigitalTwin.PREFIX + "twin";

	@ManyToOne
	@JoinColumn(name=COL_DIGITAL_TWIN_FK)
	private DigitalTwin twin;

	public AlarmTwin() {
	}

	public AlarmTwin(DigitalTwin twin, LocalDateTime time, String type, String message, AlarmGravity gravity) {
		super(time, type, message, gravity);
		this.twin = twin;
	}

	public DigitalTwin getTwin() {
		return twin;
	}

	public void setTwin(DigitalTwin twin) {
		this.twin = twin;
	}

	@Override
	public String getSourceNaturalId() {
		return twin.getName();
	}
}
