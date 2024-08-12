package com.lasrosas.iot.ingestor.domain.model.digitalTwin;

import com.lasrosas.iot.ingestor.domain.model.LongDomain;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Setter
@Getter
@SuperBuilder
public abstract class ReactorReceiver extends LongDomain {
	private DigitalTwin targetTwin;
	private String sourceSensor;
	private String sourceThing;
	private String sourceTwin;
	private ReactorReceiverType type;
	private String readable;
}
