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
		"devAddr": "02000008"
	}
 */
@Getter
@Setter
@SuperBuilder
public class LorawanMessageJoin extends LorawanMessage {
}
