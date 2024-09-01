package com.lasrosas.iot.ingestor.adapters.persisters.jpa.digitalTwin.entities;

import com.lasrosas.iot.ingestor.adapters.persisters.jpa.shared.LongEntity;
import com.lasrosas.iot.ingestor.adapters.persisters.jpa.thing.entities.ThingEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = ReactorReceiverEntity.TABLE)
@AttributeOverrides({ @AttributeOverride(column = @Column(name = ReactorReceiverEntity.COL_TECHID), name = LongEntity.PROP_TECHID), })
@Getter
@Setter
@SuperBuilder
public class ReactorReceiverEntity extends LongEntity {
	public static final String TABLE = "t_dtw_reactor_receiver";
	public static final String PREFIX = "rre_";
	public static final String PREFIX_FK = PREFIX + "fk_";

	public static final String COL_TECHID = PREFIX + "techid";
	public static final String COL_READABLE = PREFIX + "readable";
	public static final String COL_TARGET_TWIN_FK = PREFIX_FK + DigitalTwinEntity.PREFIX + "target_twin";
	public static final String COL_TYPE_FK = PREFIX_FK + ReactorReceiverTypeEntity.PREFIX + "type";
	public static final String COL_SOURCE_THING_FK = PREFIX_FK + ThingEntity.PREFIX + "source_thing";
	public static final String COL_SOURCE_TWIN_FK = PREFIX_FK + DigitalTwinEntity.PREFIX + "source_twin";
	public static final String COL_SOURCE_SENSOR = PREFIX + "source_sensor";

	public static final String PROP_SOURCE_TWIN = "sourceTwin";
	public static final String PROP_TYPE = "type";

	@ManyToOne
	@JoinColumn(name= COL_TARGET_TWIN_FK)
	private DigitalTwinEntity targetTwin;

	@Column(name= COL_SOURCE_SENSOR)
	private String sourceSensor;

	@ManyToOne
	@JoinColumn(name=COL_SOURCE_THING_FK, nullable = true)
	private ThingEntity sourceThing;

	@ManyToOne
	@JoinColumn(name=COL_SOURCE_TWIN_FK, nullable = true)
	private DigitalTwinEntity sourceTwin;

	@ManyToOne
	@JoinColumn(name=COL_TYPE_FK)
	private ReactorReceiverTypeEntity type;

	@Column(name=COL_READABLE)
	private String readable;
}
