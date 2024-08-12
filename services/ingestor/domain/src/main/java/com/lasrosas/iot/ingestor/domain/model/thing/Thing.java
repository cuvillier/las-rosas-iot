package com.lasrosas.iot.ingestor.domain.model.thing;

import com.lasrosas.iot.ingestor.domain.model.LongDomain;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import lombok.extern.slf4j.Slf4j;

@Getter
@Setter
@SuperBuilder
@Slf4j
@NoArgsConstructor
public class Thing extends LongDomain {

	private String naturalid;
	private String readable;
	private Integer connectionTimeout;

	private ThingGateway gateway;
	private ThingType type;
	private ThingProxy proxy;

	public enum AdminState {
		CONNECTED,
		DISCONNECTED,
		DISABLED,
		REMOVED
	}

	private AdminState adminState;

	@Builder.Default
	private boolean discoverable = false;
}
