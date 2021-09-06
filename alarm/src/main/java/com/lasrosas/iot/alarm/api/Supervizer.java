package com.lasrosas.iot.alarm.api;

import org.springframework.messaging.Message;

public interface Supervizer {
	void check(long tsrTechid, Message<?> value);
}
