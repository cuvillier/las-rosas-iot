package com.lasrosas.iot.core.database.entities.dtw;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

@Entity
@Table(name=DynamicTwin.TABLE)
@PrimaryKeyJoinColumn(name=DynamicTwin.COL_TECHID)
@DiscriminatorValue(DynamicTwin.DISCRIMINATOR)
public class DynamicTwin extends DigitalTwin {
	public static final String TABLE = "t_dtw_dynamic_twin";
	public static final String PREFIX = "ytw_";
	public static final String DISCRIMINATOR = "dyn";
}
