package com.lasrosas.iot.alarm.database.entity;

import org.springframework.messaging.Message;

import com.lasrosas.iot.alarm.database.entity.Alarm.State;
import com.lasrosas.iot.core.shared.utils.LasRosasHeaders;

public class DataObserver {
	private String schema;
	private String id;

	protected Alarm.State process(Message<?> imessage) {
		if( isConcerned(imessage) ) {
			return computeState(imessage);
		}

		return null;
	}

	State computeState(Message<?> imessage) {
		 String schema = LasRosasHeaders.schema(imessage).get();
		 if( !schema.equals(this.schema) ) return null;

		 if(LasRosasHeaders.naturalId(imessage).isEmpty()) return null;
		 return null;////////////////continue
	}

	boolean isConcerned(Message<?> imessage) {
		return false;
	}
}
