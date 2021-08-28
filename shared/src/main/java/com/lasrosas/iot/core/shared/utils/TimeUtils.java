package com.lasrosas.iot.core.shared.utils;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.TimeZone;

public class TimeUtils {
	public static Long timestamp(LocalDateTime time) {
		if(time == null) return null;

		return Timestamp.valueOf(time).getTime();
	}

	public static LocalDateTime time(Long timestamp) {
		if( timestamp == null) return null;
		return LocalDateTime.ofInstant(
				Instant.ofEpochMilli(timestamp),
				TimeZone.getDefault().toZoneId());
	}
}
