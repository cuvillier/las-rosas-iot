package com.lasrosas.iot.core.shared.telemetry;

import java.util.Arrays;
import java.util.List;

public class TelemetryGroup extends Telemetry {
	private final List<Telemetry> telemetries;

	public TelemetryGroup(Telemetry ... telemetries) {
		this.telemetries = Arrays.asList(telemetries);
	}

	public List<Telemetry> telemetries() {
		return telemetries;
	}
}
