package com.lasrosas.iot.ingestor.domain.model.timeserie;

import com.lasrosas.iot.ingestor.domain.model.LongDomain;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Setter
@Getter
@SuperBuilder
public class TimeSeriePoint extends LongDomain {
	private TimeSerie timeSerie;
	private String correlationId;
	private LocalDateTime time;
	private String value;
}
