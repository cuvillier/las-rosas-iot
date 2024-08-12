package com.lasrosas.iot.ingestor.adapters.persisters.jpa.digitalTwin.entities;

import com.lasrosas.iot.ingestor.adapters.persisters.jpa.shared.LongEntity;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@Table(name = DigitalTwinTypeEntity.TABLE)
@AttributeOverrides({ @AttributeOverride(column = @Column(name = DigitalTwinTypeEntity.COL_TECHID), name = LongEntity.PROP_TECHID), })
@DiscriminatorColumn(name = DigitalTwinTypeEntity.COL_DISCRIMINATOR)
@Getter
@Setter
@SuperBuilder
public abstract class DigitalTwinTypeEntity extends LongEntity {
	public static final String TABLE = "t_dtw_digital_twin_type";
	public static final String PREFIX = "twt_";
	public static final String PREFIX_FK = PREFIX + "fk_";
	public static final String COL_DISCRIMINATOR = PREFIX + "discriminator";

	public static final String COL_TECHID = PREFIX + "techid";
	public static final String COL_NAME = PREFIX + "name";
	public static final String COL_PUBLISH_ONTHOLOGY = PREFIX + "publish_onthology";
	
	public static final String COL_MAY_HAVE_CHILDREN = PREFIX + "may_have_children";
	public static final String COL_SPACE_FK = PREFIX_FK + DigitalSpaceEntity.PREFIX + "space";

	public static final String PROP_SPACE = "space";

	@ManyToOne
	@JoinColumn(name=COL_SPACE_FK)
	private DigitalSpaceEntity space;

	@OneToMany(mappedBy = DigitalTwinEntity.PROP_TYPE)
	@Builder.Default
	private List<DigitalTwinEntity> twins = new ArrayList<>();

	@Column(name=COL_NAME)
	private String name;
}
