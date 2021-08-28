package com.lasrosas.iot.core.ingestor.sensors.api;

import java.time.LocalDateTime;

import com.lasrosas.iot.core.shared.telemetry.NotPartOfState;

public class ThingDataMessage {

	@NotPartOfState
	private long thingid;

	@NotPartOfState
	private LocalDateTime time;

	public long getThingid() {
		return thingid;
	}
	public void setThingid(long thingid) {
		this.thingid = thingid;
	}
	public LocalDateTime getTime() {
		return time;
	}
	public void setTime(LocalDateTime time) {
		this.time = time;
	}
}
