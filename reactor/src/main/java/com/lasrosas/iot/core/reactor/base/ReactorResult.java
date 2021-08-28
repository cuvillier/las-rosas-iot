package com.lasrosas.iot.core.reactor.base;

import com.google.gson.JsonObject;

public class ReactorResult {
	private final JsonObject json;
	private final String schema;

	public ReactorResult(JsonObject json, String schema) {
		super();
		this.json = json;
		this.schema = schema;
	}

	public JsonObject getJson() {
		return json;
	}
	public String getSchema() {
		return schema;
	}
}
