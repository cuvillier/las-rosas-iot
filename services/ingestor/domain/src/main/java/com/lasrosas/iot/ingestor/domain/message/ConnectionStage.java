package com.lasrosas.iot.ingestor.domain.message;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
public class ConnectionStage extends BaseMessage {
	public static final int JOINING = 1;
	public static final int JOINED= 2;

	private final int stage;
}
