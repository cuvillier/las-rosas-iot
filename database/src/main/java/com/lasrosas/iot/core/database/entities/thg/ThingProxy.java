package com.lasrosas.iot.core.database.entities.thg;

import java.time.LocalDateTime;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.lasrosas.iot.core.database.entities.shared.BaseEntity;

@Entity
@Table(name = ThingProxy.TABLE)
@AttributeOverrides({ @AttributeOverride(column = @Column(name = ThingProxy.COL_TECHID), name = BaseEntity.PROP_TECHID), })
@JsonIgnoreProperties({ "thing" })
public class ThingProxy extends BaseEntity {
	public static final String TABLE = "t_thg_thing_proxy";
	public static final String PREFIX = "thp_";
	public static final String PREFIX_FK = PREFIX + "fk_";

	public static final String COL_TECHID = PREFIX + "techid";
	public static final String COL_BATTERY_STATE = PREFIX + "battery_state";
	public static final String COL_CONNECTED = PREFIX + "connected";
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

	public static final int CONNECTED = 1;
	public static final int DISCONNECTED = 0;

	@Column(name=COL_CONNECTED)
	private int connected;

	@Column(name=COL_LAST_SEEN)
	private LocalDateTime lastSeen;

	@Column(name=COL_CONFIG)
	private String config;

	@Column(name=COL_VALUES)
	@JsonIgnore
	private String values;

	public Thing getThing() {
		return thing;
	}

	public int getConnected() {
		return connected;
	}

	public void setConnected(int connected) {
		this.connected = connected;
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

	public boolean isConnected() {
		return connected == CONNECTED;
	}
}
