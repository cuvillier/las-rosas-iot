package com.lasrosas.iot.ingestor.domain.model.message;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
public class ConnectionStage extends ThingMessage {
	public static final int JOINING = 1;
	public static final int JOINED= 2;

	private final int stage;
}
