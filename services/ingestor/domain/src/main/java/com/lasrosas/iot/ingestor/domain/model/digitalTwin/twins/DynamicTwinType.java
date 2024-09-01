package com.lasrosas.iot.ingestor.domain.model.digitalTwin.twins;

import com.lasrosas.iot.ingestor.domain.model.digitalTwin.DigitalTwinType;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.List;

@SuperBuilder
@Getter
@Setter
public class DynamicTwinType extends DigitalTwinType {
	private DynamicTwinType superType;
	private List<DynamicTwinType> subtypes;
	private boolean concrete;
}
