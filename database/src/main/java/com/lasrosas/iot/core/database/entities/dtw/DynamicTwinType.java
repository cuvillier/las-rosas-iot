package com.lasrosas.iot.core.database.entities.dtw;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

@Entity
@DiscriminatorValue(DynamicTwinType.DISCRIMINATOR)
public class DynamicTwinType extends DigitalTwinType {
	public static final String DISCRIMINATOR = "dyn";
	public static final String PREFIX = "dtt_";
	public static final String PREFIX_FK = PREFIX + "fk_";
	
	public static final String COL_SUPER_TYPE_FK = PREFIX_FK + DynamicTwinType.PREFIX + "super_type";
	public static final String COL_CONCRETE = PREFIX + "concrete";
	public static final String PROP_SUPER_TYPE = "superType";

	@ManyToOne
	@JoinColumn(name=COL_SUPER_TYPE_FK)
	private DynamicTwinType superType;

	@OneToMany(mappedBy = PROP_SUPER_TYPE)
	private List<DynamicTwinType> subtypes;

	@Column(name=COL_CONCRETE)
	private boolean concrete;

	public boolean isConcrete() {
		return concrete;
	}

	public void setConcrete(boolean concrete) {
		this.concrete = concrete;
	}

	public DynamicTwinType getSuperType() {
		return superType;
	}

	public void setSuperType(DynamicTwinType superType) {
		this.superType = superType;
	}

	public List<DynamicTwinType> getSubtypes() {
		return subtypes;
	}

	public void setSubtypes(List<DynamicTwinType> subtypes) {
		this.subtypes = subtypes;
	}
}
