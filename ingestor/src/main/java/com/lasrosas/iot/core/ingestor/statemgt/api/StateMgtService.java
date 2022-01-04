package com.lasrosas.iot.core.ingestor.statemgt.api;

import org.springframework.messaging.Message;

import com.lasrosas.iot.core.shared.telemetry.ConnectionState;
import com.lasrosas.iot.core.shared.telemetry.StateMessage;

public interface StateMgtService {
	ConnectionState handleStateMessage(Message<StateMessage> message);
}
