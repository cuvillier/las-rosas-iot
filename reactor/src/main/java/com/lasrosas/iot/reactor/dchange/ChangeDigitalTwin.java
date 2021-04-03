package com.lasrosas.iot.reactor.dchange;

import java.time.LocalDateTime;

import com.google.gson.JsonObject;
import com.lasrosas.iot.database.entities.dtw.DigitalTwin;
import com.lasrosas.iot.mqtt.rules.RuleResult;

public class ChangeDigitalTwin extends RuleResult<DigitalTwin> {
	private final LocalDateTime time;
	private final String schema;
	private final JsonObject values;

	public ChangeDigitalTwin(LocalDateTime time, String schema, JsonObject values) {
		super();
		this.time = time;
		this.schema = schema;
		this.values = values;
	}
	public LocalDateTime getTime() {
		return time;
	}
	public String getSchema() {
		return schema;
	}
	public JsonObject getValues() {
		return values;
	}

}
