package com.lasrosas.iot.database.entities.alrm;

import java.time.LocalDateTime;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

import com.lasrosas.iot.database.entities.dtw.DigitalTwin;

@Entity
@DiscriminatorValue(TwinAlarm.DISCRIMINATOR)
public class TwinAlarm extends Alarm {

	public static final String PREFIX = "TWA_";
	public static final String PREFIX_FK = PREFIX + "FK_";

	public static final String DISCRIMINATOR = "TWI";
	public static final String COL_TWIN_FK = PREFIX_FK + DigitalTwin.PREFIX + "TWIN";
	public static final String PROP_TWIN = "twin";

	@OneToOne
	@JoinColumn(name=COL_TWIN_FK)
	private DigitalTwin twin;

	public TwinAlarm() {
		super();
	}

	public TwinAlarm(DigitalTwin twin, AlarmType type, LocalDateTime time) {
		super(type, time);
		this.twin = twin;
	}
	
	public DigitalTwin getTwin() {
		return twin;
	}

	public void setTwin(DigitalTwin twin) {
		this.twin = twin;
	}
}
