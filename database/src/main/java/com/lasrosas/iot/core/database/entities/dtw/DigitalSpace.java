package com.lasrosas.iot.core.database.entities.dtw;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.lasrosas.iot.core.database.entities.shared.BaseEntity;

@Entity
@Table(name = DigitalSpace.TABLE)
@AttributeOverrides({ @AttributeOverride(column = @Column(name = DigitalSpace.COL_TECHID), name = BaseEntity.PROP_TECHID), })
public class DigitalSpace extends BaseEntity {
	public static final String TABLE = "t_dtw_space";
	public static final String PREFIX = "spa_";
	public static final String PREFIX_FK = PREFIX + "fk_";

	public static final String COL_TECHID = PREFIX + "techid";
	public static final String COL_NAME = PREFIX + "name";

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
