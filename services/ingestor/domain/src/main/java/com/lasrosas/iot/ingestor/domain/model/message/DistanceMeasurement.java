package com.lasrosas.iot.ingestor.domain.model.message;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Setter
@Getter
@SuperBuilder
public class DistanceMeasurement extends BaseMessage {
	private Double distance;
}
