package com.lasrosas.iot.database.entities.shared;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
public class BaseEntity {
	public static final String PROP_TECHID = "techid";

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long techid;

	public long getTechid() {
		return techid;
	}

	public void setTechid(long techid) {
		this.techid = techid;
	}

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
		BaseEntity other = (BaseEntity) obj;
		if (techid != other.techid)
			return false;
		return true;
	}

}
