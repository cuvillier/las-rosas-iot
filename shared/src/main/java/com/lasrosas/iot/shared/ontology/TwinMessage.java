package com.lasrosas.iot.shared.ontology;

import java.time.LocalDateTime;

public class TwinMessage {
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
