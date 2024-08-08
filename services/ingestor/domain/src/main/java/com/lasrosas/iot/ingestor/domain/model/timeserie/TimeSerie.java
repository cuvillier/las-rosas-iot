package com.lasrosas.iot.ingestor.domain.model.timeserie;

import com.lasrosas.iot.ingestor.domain.model.LongDomain;
import com.lasrosas.iot.ingestor.domain.model.thing.Thing;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
public class TimeSerie extends LongDomain {
	private Thing thing;
	private String sensor;
	private TimeSerieType type;
	private TimeSeriePoint currentValue;
	private String influxdbMeasurement;
}
