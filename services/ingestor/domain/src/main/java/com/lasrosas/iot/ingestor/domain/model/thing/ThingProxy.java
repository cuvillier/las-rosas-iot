package com.lasrosas.iot.ingestor.domain.model.thing;

import com.lasrosas.iot.ingestor.domain.model.LongDomain;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;

@Getter
@Setter
@SuperBuilder
@Slf4j
@NoArgsConstructor
public class ThingProxy extends LongDomain {

	public enum BatteryState {
		OK,
		WARNING,
		ALARM
	}

	private BatteryState batteryState;
	private Integer batteryLevel;

	public enum ConnectionState {
		CONNECTED,
		DISCONNECTED
	}
	private ConnectionState connectionState;
	private LocalDateTime lastSeen;
	private String config;
	private String values;

	public boolean isConnected() {
		return connectionState != null && connectionState == ConnectionState.CONNECTED;
	}
}
