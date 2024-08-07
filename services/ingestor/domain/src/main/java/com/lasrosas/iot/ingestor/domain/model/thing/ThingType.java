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
@Slf4j
@NoArgsConstructor
public class ThingType extends LongDomain {
	private String manufacturer;
	private String model;
	private String readable;
	private Double batteryMinPercentage;
}
