package com.lasrosas.iot.services.db.entities.dtw;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.lasrosas.iot.services.db.entities.shared.BaseEntity;

@Entity
@Table(name = DigitalSpace.TABLE)
@AttributeOverrides({ @AttributeOverride(column = @Column(name = DigitalSpace.COL_TECHID), name = BaseEntity.PROP_TECHID), })
public class DigitalSpace extends BaseEntity {
	public static final String TABLE = "T_DTW_SPACE";
	public static final String PREFIX = "SPA_";
	public static final String PREFIX_FK = PREFIX + "FK_";

	public static final String COL_TECHID = PREFIX + "TECHID";
	public static final String COL_NAME = PREFIX + "NAME";

	@Column(name=COL_NAME)
	private String name;

	@OneToMany(mappedBy = DigitalTwinType.PROP_SPACE)
	private List<DigitalTwinType> digitalTwinTypes = new ArrayList<>();

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<DigitalTwinType> getDigitalTwinTypes() {
		return digitalTwinTypes;
	}

	public void setDigitalTwinTypes(List<DigitalTwinType> digitalTwinTypes) {
		this.digitalTwinTypes = digitalTwinTypes;
	}
}
