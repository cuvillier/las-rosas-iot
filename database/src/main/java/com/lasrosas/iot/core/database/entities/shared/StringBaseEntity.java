package com.lasrosas.iot.core.database.entities.shared;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
public class StringBaseEntity {
	public static final String PROP_FUNCID = "techid";

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private String funcid;

	public String getFuncid() {
		return funcid;
	}

	public void setTechid(String funcid) {
		this.funcid = funcid;
	}

	@Override
	public int hashCode() {
		return funcid.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		return funcid.equals(obj);
	}
}
