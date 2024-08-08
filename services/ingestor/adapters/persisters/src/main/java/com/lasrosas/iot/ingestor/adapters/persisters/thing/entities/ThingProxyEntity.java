package com.lasrosas.iot.ingestor.adapters.persisters.thing.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.lasrosas.iot.ingestor.adapters.persisters.shared.LongEntity;
import com.lasrosas.iot.ingestor.shared.JsonUtils;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;

@Entity
@Table(name = ThingProxyEntity.TABLE)
@AttributeOverrides({ @AttributeOverride(column = @Column(name = ThingProxyEntity.COL_TECHID), name = LongEntity.PROP_TECHID), })
@JsonIgnoreProperties({ "thing" })
@Getter
@Setter
@SuperBuilder
@Slf4j
@NoArgsConstructor
public class ThingProxyEntity extends LongEntity {
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
	public static final String COL_THING_FK = PREFIX_FK + ThingEntity.PREFIX + "thing";
	public static final String PROP_THING = "thing";

	public enum BatteryState {
		OK,
		WARNING,
		ALARM
	}

	@Column(name=COL_BATTERY_STATE)
	@Enumerated(EnumType.STRING)
	private BatteryState batteryState;

	@Column(name=COL_BATTERY_LEVEL)
	private Integer batteryLevel;

	public enum ConnectionState {
		CONNECTED,
		DISCONNECTED
	}

	@Column(name= COL_CONNECTION_STATE)
	@Enumerated(EnumType.STRING)
	private ConnectionState connectionState;

	@Column(name=COL_LAST_SEEN)
	private LocalDateTime lastSeen;

	@Column(name=COL_CONFIG)
	private String config;

	@Column(name=COL_VALUES)
	@JsonIgnore
	private String values;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name=COL_THING_FK)
	private ThingEntity thing;

	public boolean isConnected() {
		return connectionState != null && connectionState == ConnectionState.CONNECTED;
	}

	public ObjectNode getValuesAsObjectNode() {
		if(values == null) return null;
		return (ObjectNode)JsonUtils.toObjectNode(values);
	}
}
