package com.lasrosas.iot.database.entities.dtw;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

@Entity
@Table(name=DynamicTwin.TABLE)
@PrimaryKeyJoinColumn(name=DynamicTwin.COL_TECHID)
@DiscriminatorValue(DynamicTwin.DISCRIMINATOR)
public class DynamicTwin extends DigitalTwin {
	public static final String TABLE = "T_DTW_DYNAMIC_TWIN";
	public static final String PREFIX = "YTW_";
	public static final String DISCRIMINATOR = "DYN";
}
