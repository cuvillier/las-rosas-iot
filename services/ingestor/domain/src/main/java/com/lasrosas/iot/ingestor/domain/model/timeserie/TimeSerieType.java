package com.lasrosas.iot.ingestor.domain.model.timeserie;

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
public class TimeSerieType extends LongDomain {

	@Builder.Default
	private List<TimeSerie> timeSeries = new ArrayList<>();
	private String schema;
}
