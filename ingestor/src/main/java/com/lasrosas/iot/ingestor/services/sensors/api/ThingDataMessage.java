package com.lasrosas.iot.ingestor.services.sensors.api;

import java.time.LocalDateTime;

public class ThingDataMessage {
	private long thingid;
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
