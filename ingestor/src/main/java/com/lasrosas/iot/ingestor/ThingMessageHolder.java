package com.lasrosas.iot.ingestor;

import java.time.LocalDateTime;

public class ThingMessageHolder {
	private String schema;
	private String sensor;
	private Long thingId;
	private Long twinId;
	private LocalDateTime time;
	private Object message;

	public ThingMessageHolder(String schema, String sensor, Object message) {
		super();

		this.schema = schema;
		this.sensor = sensor;
		this.message = message;
	}
	public String getSchema() {
		return schema;
	}
	public void setSchema(String schema) {
		this.schema = schema;
	}
	public String getSensor() {
		return sensor;
	}
	public void setSensor(String sensor) {
		this.sensor = sensor;
	}
	public Object getMessage() {
		return message;
	}
	public void setMessage(Object message) {
		this.message = message;
	}
}
