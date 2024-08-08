package com.lasrosas.iot.ingestor.domain.message;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Setter
@Getter
@SuperBuilder
public class AirEnvironment extends BaseMessage {
	private Double temperature;
	private Double humidity;
	private Double light;
}
