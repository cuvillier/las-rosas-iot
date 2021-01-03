package com.lasrosas.iot.shared.rules;

import java.time.LocalDateTime;
import java.util.Arrays;

public class DataChange {

	public static class NewValue {
		private String attribute;
		private String oldValue;
		private LocalDateTime time;
		private String newValue;

		public NewValue(LocalDateTime time, String attribute, String oldValue, String newValue) {
			super();
			this.attribute = attribute;
			this.oldValue = oldValue;
			this.time = time;
			this.newValue = newValue;
		}

		public String getAttribute() {
			return attribute;
		}
		public String getOldValue() {
			return oldValue;
		}
		public LocalDateTime getTime() {
			return time;
		}
		public String getNewValue() {
			return newValue;
		}
	}

	private String entityType;
	private long entityId;
	private NewValue [] newValues;

	public DataChange() {
	}

	public DataChange(String entityType, long entityId, NewValue ... newValues) {
		super();
		this.entityType = entityType;
		this.entityId = entityId;
		this.newValues = Arrays.copyOf(newValues, newValues.length);
	}

	public String getEntityType() {
		return entityType;
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
