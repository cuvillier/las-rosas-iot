package com.lasrosas.iot.ingestor.domain.model.digitalTwin;

import com.lasrosas.iot.ingestor.domain.model.LongDomain;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@SuperBuilder
public abstract class DigitalTwin extends LongDomain {
	private DigitalTwinType type;

	@Builder.Default
	private List<ReactorReceiver> receivers = new ArrayList<>();

	private String properties;
	private String name;
}
