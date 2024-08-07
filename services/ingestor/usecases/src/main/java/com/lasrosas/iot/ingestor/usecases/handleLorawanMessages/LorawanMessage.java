package com.lasrosas.iot.ingestor.usecases.handleLorawanMessages;

import com.lasrosas.iot.ingestor.domain.model.message.MessageOrigin;
import com.lasrosas.iot.ingestor.domain.model.message.ThingMessage;
import com.lasrosas.iot.ingestor.usecases.handleLorawanMessages.thingDrivers.dragino.DraginoLHT65Frame;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
public abstract class LorawanMessage implements MessageOrigin {
	private String thingNaturalid;
	private String correlationId;
	private LocalDateTime time;
	private long timestamp;

	private String devEUI;
	public String applicationID;
	public String applicationName;
	public String deviceName;
}
