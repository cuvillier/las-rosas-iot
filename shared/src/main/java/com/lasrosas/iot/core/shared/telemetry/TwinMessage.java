package com.lasrosas.iot.core.shared.telemetry;

import java.time.LocalDateTime;

public class TwinMessage extends Telemetry {
	private String schema;
	private LocalDateTime time;
	private Object value;

	public TwinMessage(String schema, LocalDateTime time, Object value) {
		super();
		this.schema = schema;
		this.time = time;
		this.value = value;
	}

	public String getSchema() {
		return schema;
	}
	public LocalDateTime getTime() {
		return time;
	}
	public Object getValue() {
		return value;
	}

}
