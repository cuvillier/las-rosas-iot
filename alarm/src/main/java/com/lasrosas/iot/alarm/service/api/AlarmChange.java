package com.lasrosas.iot.alarm.service.api;

import java.time.LocalDateTime;

import com.lasrosas.iot.alarm.database.entity.AlarmGravity;
import com.lasrosas.iot.alarm.database.entity.AlarmState;

public class AlarmChange {
	private LocalDateTime time;
	private AlarmState state;
	private String dataType;
	private String cause;
	private AlarmGravity gravity;

	public AlarmChange(LocalDateTime time, AlarmState state, String dataType, String cause, AlarmGravity gravity) {
		this.time = time;
		this.state = state;
		this.dataType = dataType;
		this.cause = cause;
		this.gravity = gravity;
	}

	public String getCause() {
		return cause;
	}
	public void setCause(String cause) {
		this.cause = cause;
	}
	public String getDataType() {
		return dataType;
	}
	public void setDataType(String dataType) {
		this.dataType = dataType;
	}
	public AlarmState getState() {
		return state;
	}
	public void setState(AlarmState state) {
		this.state = state;
	}
	public AlarmGravity getGravity() {
		return gravity;
	}
	public void setGravity(AlarmGravity gravity) {
		this.gravity = gravity;
	}
	public LocalDateTime getTime() {
		return time;
	}
	public void setTime(LocalDateTime time) {
		this.time = time;
	}

}
