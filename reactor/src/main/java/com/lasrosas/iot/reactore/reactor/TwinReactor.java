package com.lasrosas.iot.reactore.reactor;

import java.util.List;

import com.google.gson.JsonObject;
import com.lasrosas.iot.database.entities.dtw.DigitalTwin;

public abstract class TwinReactor {
	public abstract List<ReactorResult> react(DigitalTwin twin, List<ReactorReceiverValue> values);
}
