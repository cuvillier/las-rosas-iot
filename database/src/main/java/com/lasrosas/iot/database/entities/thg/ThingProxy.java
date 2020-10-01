package com.lasrosas.iot.database.entities.thg;

import java.time.LocalDateTime;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.lasrosas.iot.database.entities.shared.BaseEntity;

@Entity
@Table(name = ThingProxy.TABLE)
@AttributeOverrides({ @AttributeOverride(column = @Column(name = ThingProxy.COL_TECHID), name = BaseEntity.PROP_TECHID), })
public class ThingProxy extends BaseEntity {
	public static final String TABLE = "T_THG_THING_PROXY";
	public static final String PREFIX = "THP_";
	public static final String PREFIX_FK = PREFIX + "FK_";

	public static final String COL_TECHID = PREFIX + "TECHID";
	public static final String COL_BATTERY_STATE = PREFIX + "BATTERY_STATE";
	public static final String COL_CONNECTION_STATE = PREFIX + "CONNECTION_STATE";
	public static final String COL_BATTERY_LEVEL = PREFIX + "BATTERY_LEVEL";
	public static final String COL_LAST_SEEN = PREFIX + "LAST_SEEN";
	public static final String COL_CONFIG = PREFIX + "CONFIG";
	public static final String COL_VALUES = PREFIX + "VALUES";
	public static final String COL_THING_FK = PREFIX_FK + Thing.PREFIX + "THING";
	public static final String PROP_THING = "thing";

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name=COL_THING_FK)
	private Thing thing;

	@Column(name=COL_BATTERY_STATE)
	private int batteryState;

	@Column(name=COL_BATTERY_LEVEL)
	private Integer batteryLevel;

	@Column(name=COL_CONNECTION_STATE)
	private boolean connectionState;
	
	@Column(name=COL_LAST_SEEN)
	private LocalDateTime lastSeen;

	@Column(name=COL_CONFIG)
	private String config;

	@Column(name=COL_VALUES)
	private String values;

	public Thing getThing() {
		return thing;
	}

	public void setThing(Thing thing) {
		this.thing = thing;
	}

	public int getBatteryState() {
		return batteryState;
	}

	public void setBatteryState(int batteryState) {
		this.batteryState = batteryState;
	}

	public Integer getBatteryLevel() {
		return batteryLevel;
	}

	public void setBatteryLevel(Integer batteryLevel) {
		this.batteryLevel = batteryLevel;
	}

	public boolean isConnectionState() {
		return connectionState;
	}

	public void setConnectionState(boolean connectionState) {
		this.connectionState = connectionState;
	}

	public LocalDateTime getLastSeen() {
		return lastSeen;
	}

	public void setLastSeen(LocalDateTime lastSeen) {
		this.lastSeen = lastSeen;
	}

	public String getConfig() {
		return config;
	}

	public void setConfig(String config) {
		this.config = config;
	}

	public String getValues() {
		return values;
	}

	public void setValues(String values) {
		this.values = values;
	}
}
