package com.lasrosas.iot.core.ingestor.statemgt.api;

import com.lasrosas.iot.core.shared.telemetry.ConnectionState;

public interface StateNotifictionCallback {
	void notification(ConnectionState stae);
}
