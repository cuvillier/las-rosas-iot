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
	public static final String TABLE = "t_thg_thing_proxy";
	public static final String PREFIX = "thp_";
	public static final String PREFIX_FK = PREFIX + "fk_";

	public static final String COL_TECHID = PREFIX + "techid";
	public static final String COL_BATTERY_STATE = PREFIX + "battery_state";
	public static final String COL_CONNECTION_STATE = PREFIX + "connection_state";
	public static final String COL_BATTERY_LEVEL = PREFIX + "battery_level";
	public static final String COL_LAST_SEEN = PREFIX + "last_seen";
	public static final String COL_CONFIG = PREFIX + "config";
	public static final String COL_VALUES = PREFIX + "values";
	public static final String COL_THING_FK = PREFIX_FK + Thing.PREFIX + "thing";
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
