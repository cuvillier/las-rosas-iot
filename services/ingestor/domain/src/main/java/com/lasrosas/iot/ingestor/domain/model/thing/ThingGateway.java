package com.lasrosas.iot.ingestor.domain.model.thing;

import com.lasrosas.iot.ingestor.domain.model.LongDomain;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import lombok.extern.slf4j.Slf4j;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@Slf4j
public class ThingGateway extends LongDomain {
	private String naturalid;
	private String type;
	private String driver;
}
