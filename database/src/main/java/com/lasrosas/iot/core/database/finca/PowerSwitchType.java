package com.lasrosas.iot.core.database.finca;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import com.lasrosas.iot.core.database.entities.dtw.DigitalTwinType;

@Entity
@DiscriminatorValue(PowerSwitchType.DISCRIMINATOR)
public class PowerSwitchType extends DigitalTwinType {
	public static final String DISCRIMINATOR = "swt";

	public static final String PREFIX = "swt_";
	public static final String PREFIX_FK = PREFIX + "fk_";
	public static final String COL_MAX_STATE = PREFIX + "maxState";
	public static final String COL_TECHID = PREFIX + "techid";

	@Column(name=COL_MAX_STATE)
	private int maxState = 2;

	public int getMaxState() {
		return maxState;
	}

	public void setMaxState(int maxState) {
		this.maxState = maxState;
	}
}