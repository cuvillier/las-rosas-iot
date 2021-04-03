package com.lasrosas.iot.mqtt.rules;

import java.util.Arrays;

public class DataChangeMatcher {

	private final String entityType;
	private final Long entityId;
	private final String [] attributes;

	public DataChangeMatcher(String entityType, Long entityId, String ... attributes) {
		this.entityType = entityType;
		this.entityId = entityId;

		if(attributes == null)
			this.attributes = null;
		else
			this.attributes = Arrays.copyOf(attributes, attributes.length);
	}

	public boolean match(DataChange dataChange) {
		return match(getEntityType(), dataChange.getEntityId(), dataChange.getAttributes());
	}

	public boolean match(String entityType, Long entityId, String ... attributes) {

		if( this.entityType != null && !this.entityType.equals(entityType)) {
			return false;
		}

		if( this.entityId != null && !this.entityId.equals(entityId)) {
			return false;
		}

		if( attributes != null ) {
			boolean found = false;

			foundLoop: for(var attribute: attributes) {
				for(var thisAttribute: this.attributes) {
					if(attribute.contentEquals(thisAttribute)) {
						found = true;
						break foundLoop;
					}
				}
			}

			if(!found) return false;
		}

		return true;
	}

	public String getEntityType() {
		return entityType;
	}

	public Long getEntityId() {
		return entityId;
	}

	public String[] getAttributes() {
		return attributes;
	}
}