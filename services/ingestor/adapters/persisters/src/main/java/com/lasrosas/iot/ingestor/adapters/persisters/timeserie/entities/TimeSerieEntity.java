package com.lasrosas.iot.ingestor.adapters.persisters.timeserie.entities;

import com.lasrosas.iot.ingestor.adapters.persisters.shared.LongEntity;
import com.lasrosas.iot.ingestor.adapters.persisters.thing.entities.ThingEntity;
import com.lasrosas.iot.ingestor.domain.model.thing.Thing;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = TimeSerieEntity.TABLE)
@AttributeOverrides({@AttributeOverride(column = @Column(name = TimeSerieEntity.COL_TECHID), name = LongEntity.PROP_TECHID), })
@Getter
@Setter
@SuperBuilder
public class TimeSerieEntity extends LongEntity {
	public static final String TABLE = "t_tsr_time_serie";
	public static final String PREFIX = "tsr_";
	public static final String PREFIX_FK = PREFIX + "fk_";

	public static final String COL_TECHID = PREFIX + "techid";
	public static final String COL_SENSOR = PREFIX + "sensor";
	public static final String COL_INFLUXDB_MEASUREMENT= PREFIX + "influxdb_measurement";
	public static final String COL_TYPE_FK = PREFIX_FK + TimeSerieTypeEntity.PREFIX + "type";
	public static final String COL_THING_FK = PREFIX_FK + ThingEntity.PREFIX + "thing";
//	public static final String COL_TWIN_FK = PREFIX_FK + DigitalTwinEntity.PREFIX + "twin";
	public static final String COL_CURRENT_VALUE_FK = PREFIX_FK + TimeSeriePointEntity.PREFIX + "current_value";

	public static final String PROP_TYPE = "type";

	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	@JoinColumn(name = COL_THING_FK)
	private ThingEntity thing;

	/*
	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	@JoinColumn(name = COL_TWIN_FK)
	private DigitalTwin twin;
	*/

	@Column(name=COL_SENSOR)
	private String sensor;

	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	@JoinColumn(name = COL_TYPE_FK)
	private TimeSerieTypeEntity type;

	@Builder.Default
	@OneToMany(mappedBy = TimeSeriePointEntity.PROP_TIME_SERIE, fetch = FetchType.LAZY)
	private List<TimeSeriePointEntity> points = new ArrayList<>();

	@OneToOne
	@JoinColumn(name=COL_CURRENT_VALUE_FK)
	private TimeSeriePointEntity currentValue;

	@Column(name=COL_INFLUXDB_MEASUREMENT)
	private String influxdbMeasurement;
}
