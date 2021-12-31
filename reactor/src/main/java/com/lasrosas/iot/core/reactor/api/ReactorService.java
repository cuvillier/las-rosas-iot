package com.lasrosas.iot.core.reactor.api;

import java.util.List;

import org.springframework.messaging.Message;

import com.lasrosas.iot.core.shared.telemetry.Telemetry;

public interface ReactorService {
	List<Message<?>> react(Message<? extends Telemetry> imessage);
}
