package com.lasrosas.iot.ingestor.domain.model.digitalTwin;

import com.lasrosas.iot.ingestor.domain.model.LongDomain;
import com.lasrosas.iot.ingestor.domain.model.thing.Thing;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Setter
@Getter
@SuperBuilder
public class ReactorReceiver extends LongDomain {
	private Long targetTwinId;
	private String sourceSensor;
	private Long sourceThingId;
	private Long  sourceTwinId;
	private ReactorReceiverType type;
	private String readable;
}
