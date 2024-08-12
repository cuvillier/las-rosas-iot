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
public class ReactorReceiverType extends LongDomain {
	private String role;
	private String schema;

	private ReactorType reactorType;

	@Builder.Default
	private List<ReactorReceiver> receivers = new ArrayList<>();
}
