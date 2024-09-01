package com.lasrosas.iot.ingestor.adapters.persisters.jpa.digitalTwin.entities.twins;

import com.lasrosas.iot.ingestor.adapters.persisters.jpa.digitalTwin.entities.DigitalTwinTypeEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Entity
@DiscriminatorValue(DynamicTwinTypeEntity.DISCRIMINATOR)
@SuperBuilder
@Getter
@Setter
public class DynamicTwinTypeEntity extends DigitalTwinTypeEntity {
	public static final String DISCRIMINATOR = "dyn";
	public static final String PREFIX = "dtt_";
	public static final String PREFIX_FK = PREFIX + "fk_";
	
	public static final String COL_SUPER_TYPE_FK = PREFIX_FK + DynamicTwinTypeEntity.PREFIX + "super_type";
	public static final String COL_CONCRETE = PREFIX + "concrete";
	public static final String PROP_SUPER_TYPE = "superType";

	@ManyToOne
	@JoinColumn(name=COL_SUPER_TYPE_FK)
	private DynamicTwinTypeEntity superType;

	@OneToMany(mappedBy = PROP_SUPER_TYPE)
	private List<DynamicTwinTypeEntity> subtypes;

	@Column(name=COL_CONCRETE)
	private boolean concrete;
}
