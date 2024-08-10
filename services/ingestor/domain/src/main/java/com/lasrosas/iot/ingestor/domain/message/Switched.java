package com.lasrosas.iot.ingestor.domain.message;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
public class Switched extends BaseMessage {
	public static enum State {
		OFF, ON
	}
	private State state;
}
