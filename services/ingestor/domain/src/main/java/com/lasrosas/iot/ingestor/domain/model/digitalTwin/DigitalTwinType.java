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
public abstract class DigitalTwinType extends LongDomain {
	private DigitalSpace space;

	@Builder.Default
	private List<DigitalTwin> twins = new ArrayList<>();

	private String name;
}
