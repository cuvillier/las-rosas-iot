package com.lasrosas.iot.ingestor.domain.model.message;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Setter
@Getter
@SuperBuilder
public class AirEnvironment extends ThingMessage {
	private Double temperature;
	private Double humidity;
	private Double light;
}
