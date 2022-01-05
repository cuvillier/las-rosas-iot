package com.lasrosas.iot.core.database.finca;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import com.lasrosas.iot.core.database.entities.dtw.DigitalTwinType;

@Entity
@DiscriminatorValue(MultiSwitchType.DISCRIMINATOR)
public class MultiSwitchType extends DigitalTwinType {
	public static final String DISCRIMINATOR = "mst";

	public static final String PREFIX = "mst_";
	public static final String PREFIX_FK = PREFIX + "fk_";
	public static final String COL_MAX_STATE = PREFIX + "max_state";
	public static final String COL_TECHID = PREFIX + "techid";

	@Column(name=COL_MAX_STATE)
	private Integer maxState = 2;

	public int getMaxState() {
		return maxState == null? 2: maxState;
	}

	public void setMaxState(int maxState) {
		this.maxState = maxState;
	}
}