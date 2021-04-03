package com.lasrosas.iot.mqtt.rules;

import java.time.LocalDateTime;
import java.util.Arrays;

public class DataChange {

	private LocalDateTime time;
	
	// Sensor, DigitalTwin...
	private String entityClass;

	private long entityId;

	private NewValue [] newValues;

	public static class NewValue {
		private String attribute;
		private String oldValue;
		private String newValue;

		public NewValue(String attribute, String oldValue, String newValue) {
			super();
			this.attribute = attribute;
			this.oldValue = oldValue;
			this.newValue = newValue;
		}

		public String getAttribute() {
			return attribute;
		}
		public String getOldValue() {
			return oldValue;
		}
		public String getNewValue() {
			return newValue;
		}
	}

	public DataChange() {
	}

	public DataChange(String entityClass, long entityId, LocalDateTime time, NewValue ... newValues) {
		super();

		this.entityClass = entityClass;
		this.entityId = entityId;
		this.time = time;
		this.newValues = Arrays.copyOf(newValues, newValues.length);
	}

	public LocalDateTime getTime() {
		return time;
	}

	public String getEntityClass() {
		return entityClass;
	}
	public long getEntityId() {
		return entityId;
	}
	public NewValue[] getNewValues() {
		return newValues;
	}
	public String [] getAttributes() {
		String[] result = new String[newValues.length];

		for(int i = 0; i < newValues.length; i++) {
			result[i] = newValues[i].getAttribute();
		}
		
		return result;
	}
}
