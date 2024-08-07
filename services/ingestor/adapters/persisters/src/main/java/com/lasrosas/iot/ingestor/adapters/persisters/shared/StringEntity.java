package com.lasrosas.iot.ingestor.adapters.persisters.shared;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@MappedSuperclass
@Getter
@Setter
@SuperBuilder
public class StringEntity {
	public static final String PROP_NATURALID = "narturalId";

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private String naturalId;

	@Override
	public int hashCode() {
		return naturalId.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		return naturalId.equals(obj);
	}
}
