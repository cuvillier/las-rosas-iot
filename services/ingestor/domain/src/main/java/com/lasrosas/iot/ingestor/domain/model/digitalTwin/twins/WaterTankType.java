package com.lasrosas.iot.ingestor.domain.model.digitalTwin.twins;

import com.lasrosas.iot.ingestor.domain.model.digitalTwin.DigitalTwinType;
import lombok.experimental.SuperBuilder;

@SuperBuilder
public class WaterTankType extends DigitalTwinType {
	public static final String DISCRIMINATOR = "wat";

}