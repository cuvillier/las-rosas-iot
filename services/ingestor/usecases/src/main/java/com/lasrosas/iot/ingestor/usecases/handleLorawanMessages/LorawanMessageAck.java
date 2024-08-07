package com.lasrosas.iot.ingestor.usecases.handleLorawanMessages;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

/*
	{
		"applicationID": "3",
		"applicationName": "las-rosas-iot",
		"deviceName": "MFC88-LW13IO-70b3d58ff10184b8",
		"devEUI": "70b3d58ff10184b8",
		"acknowledged": true,
		"fCnt": 54
	}
 */
@Getter
@Setter
@SuperBuilder
public class LorawanMessageAck extends LorawanMessage {
	public boolean acknowledge;
}
