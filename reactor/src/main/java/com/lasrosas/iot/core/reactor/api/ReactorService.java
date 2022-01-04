package com.lasrosas.iot.core.reactor.api;

import java.util.List;

import org.springframework.messaging.Message;

public interface ReactorService {
	List<Message<?>> react(Message<?> imessage);
}
