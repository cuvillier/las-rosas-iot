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
public class DigitalSpace extends LongDomain {
	private String name;

	@Builder.Default
	private List<DigitalTwinType> digitalTwinTypes = new ArrayList<>();
}
