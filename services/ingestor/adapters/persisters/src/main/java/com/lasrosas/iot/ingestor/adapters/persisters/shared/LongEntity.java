package com.lasrosas.iot.ingestor.adapters.persisters.shared;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@MappedSuperclass
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
public abstract class LongEntity {
	public static final String PROP_TECHID = "techid";

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long techid;

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (techid ^ (techid >>> 32));
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		LongEntity other = (LongEntity) obj;
		if (techid != other.techid)
			return false;
		return true;
	}

}
