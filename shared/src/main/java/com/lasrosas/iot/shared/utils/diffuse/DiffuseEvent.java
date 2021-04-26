package com.lasrosas.iot.shared.utils.diffuse;

public class DiffuseEvent {
	private long srcTechid;
	private String eventType;
	private long timestamp;
	private Object value;

	public DiffuseEvent(long srcTechid, String eventType, long timestamp, Object value) {
		super();
		this.srcTechid = srcTechid;
		this.eventType = eventType;
		this.timestamp = timestamp;
		this.value = value;
	}
	public long getSrcTechid() {
		return srcTechid;
	}
	public String getEventType() {
		return eventType;
	}
	public long getTimestamp() {
		return timestamp;
	}
	public Object getValue() {
		return value;
	}
	
}
