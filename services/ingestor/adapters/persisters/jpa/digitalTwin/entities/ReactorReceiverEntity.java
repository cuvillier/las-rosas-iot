package com.lasrosas.iot.ingestor.adapters.persisters.jpa.digitalTwin.entities;

import com.lasrosas.iot.ingestor.adapters.persisters.jpa.shared.LongEntity;
import com.lasrosas.iot.ingestor.adapters.persisters.jpa.thing.entities.ThingEntity;
import jakarta.persistence.*;

@Entity
@Table(name = ReactorReceiverEntity.TABLE)
@AttributeOverrides({ @AttributeOverride(column = @Column(name = ReactorReceiverEntity.COL_TECHID), name = LongEntity.PROP_TECHID), })
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = ReactorReceiverEntity.COL_DISCRIMINATOR)
public abstract class ReactorReceiverEntity extends LongEntity {
	public static final String TABLE = "t_dtw_reactor_receiver";
	public static final String PREFIX = "rre_";
	public static final String PREFIX_FK = PREFIX + "fk_";

	public static final String COL_TECHID = PREFIX + "techid";
	public static final String COL_DISCRIMINATOR = PREFIX + "discriminator";
	public static final String COL_READABLE = PREFIX + "readable";
	public static final String COL_TARGET_TWIN_FK = PREFIX_FK + DigitalTwinEntity.PREFIX + "target_twin";
	public static final String COL_TYPE_FK = PREFIX_FK + ReactorReceiverTypeEntity.PREFIX + "type";
	public static final String COL_SOURCE_THING_FK = PREFIX_FK + ThingEntity.PREFIX + "source_thing";
	public static final String COL_SOURCE_TWIN_FK = PREFIX_FK + DigitalTwinEntity.PREFIX + "source_twin";
	public static final String COL_SOURCE_SENSOR = PREFIX + "source_sensor";

	public static final String PROP_TYPE = "type";
	public static final String PROP_TWIN = "twin";

	@ManyToOne
	@JoinColumn(name= COL_TARGET_TWIN_FK)
	private DigitalTwinEntity targetTwin;

	@Column(name= COL_SOURCE_SENSOR)
	private String sourceSensor;

	@Column(name=COL_SOURCE_THING_FK)
	private String sourceThing;

	@Column(name=COL_SOURCE_TWIN_FK)
	private String sourceTwin;

	@ManyToOne
	@JoinColumn(name=COL_TYPE_FK)
	private ReactorReceiverTypeEntity type;

	@Column(name=COL_READABLE)
	private String readable;
}
