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
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = DigitalTwinEntity.TABLE)
@AttributeOverrides({ @AttributeOverride(column = @Column(name = DigitalTwinEntity.COL_TECHID), name = LongEntity.PROP_TECHID)})
@DiscriminatorColumn(name= DigitalTwinEntity.COL_DISCRIMINATOR)
@Getter
@Setter
@SuperBuilder
public /* mapstructs abstract */ class DigitalTwinEntity extends LongEntity {

	public static final String TABLE = "t_dtw_digital_twin";
	public static final String PREFIX = "twi_";
	public static final String PREFIX_FK = PREFIX + "fk_";

	public static final String COL_TECHID = PREFIX + "techid";
	public static final String COL_DISCRIMINATOR = PREFIX + "discriminator";
	public static final String COL_NAME = PREFIX + "name";
	public static final String COL_PROPERTIES = PREFIX + "properties";
	public static final String COL_TYPE_FK = PREFIX_FK + DigitalTwinTypeEntity.PREFIX + "type";

	public static final String PROP_TYPE = "type";

	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	@JoinColumn(name = COL_TYPE_FK)
	private DigitalTwinTypeEntity type;

	@OneToMany(mappedBy = ReactorReceiverEntity.PROP_SOURCE_TWIN)
	@Builder.Default
	private List<ReactorReceiverEntity> receivers = new ArrayList<>();

	@Column(name=COL_PROPERTIES)
	private String properties;

	@Column(name=COL_NAME)
	private String name;

}
