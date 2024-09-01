package com.lasrosas.iot.ingestor.domain.model.digitalTwin.twins;

import com.lasrosas.iot.ingestor.domain.model.digitalTwin.DigitalTwin;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SuperBuilder
@Getter
@Setter
public class MultiSwitch extends DigitalTwin {
	public static final Logger log = LoggerFactory.getLogger(MultiSwitch.class);

	public static final int OFF = 0;
	public static final int ON = 1;

	@Builder.Default
	private int state = OFF;

	@Builder.Default
	private int expectedState = OFF;

	@Builder.Default
	private boolean connected = false;

	@Builder.Default
	private Integer stateWhenConnect = OFF;
}
