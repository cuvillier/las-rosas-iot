package com.lasrosas.iot.ingestor.services.lora.api;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.TimeZone;

public abstract class LoraMessage {
	private LocalDateTime time;
	private String deveui;

	public LocalDateTime getTime() {
		return time;
	}

	public Long getTimestamp() {
		if(time == null) return null;

		return Timestamp.valueOf(time).getTime();
	}
	
	public void setTimestamp(Long timestamp) {
		if( timestamp == null) this.time = null;
		time = LocalDateTime.ofInstant(Instant.ofEpochMilli(timestamp ), TimeZone
		        .getDefault().toZoneId());
	}

	public void setTime(LocalDateTime time) {
		this.time = time;
	}
	public String getDeveui() {
		return deveui;
	}
	public void setDeveui(String deveui) {
		this.deveui = deveui;
	}
}

