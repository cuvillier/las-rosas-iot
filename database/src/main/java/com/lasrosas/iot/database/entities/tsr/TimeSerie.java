package com.lasrosas.iot.database.entities.tsr;

import java.util.List;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.lasrosas.iot.database.entities.dtw.DigitalTwin;
import com.lasrosas.iot.database.entities.shared.BaseEntity;
import com.lasrosas.iot.database.entities.thg.Thing;

@Entity
@Table(name = TimeSerie.TABLE)
@AttributeOverrides({
		@AttributeOverride(column = @Column(name = TimeSerie.COL_TECHID), name = BaseEntity.PROP_TECHID), })
public class TimeSerie extends BaseEntity {
	public static final String TABLE = "t_tsr_time_serie";
	public static final String PREFIX = "tsr_";
	public static final String PREFIX_FK = PREFIX + "fk_";

	public static final String COL_TECHID = PREFIX + "techid";
	public static final String COL_SENSOR = PREFIX + "sensor";
	public static final String COL_INFLUXDB_MEASUREMENT= PREFIX + "influxdb_measurement";
	public static final String COL_TYPE_FK = PREFIX_FK + TimeSerieType.PREFIX + "type";
	public static final String COL_THING_FK = PREFIX_FK + Thing.PREFIX + "thing";
	public static final String COL_TWIN_FK = PREFIX_FK + DigitalTwin.PREFIX + "twin";
	public static final String COL_CURRENT_VALUE_FK = PREFIX_FK + TimeSeriePoint.PREFIX + "current_value";

	public static final String PROP_TYPE = "type";

	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	@JoinColumn(name = COL_THING_FK)
	private Thing thing;

	@Column(name=COL_SENSOR)
	private String sensor;

	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	@JoinColumn(name = COL_TYPE_FK)
	private TimeSerieType type;

	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	@JoinColumn(name = COL_TWIN_FK)
	private DigitalTwin twin;

	@OneToMany(mappedBy = TimeSeriePoint.PROP_TIME_SERIE)
	private List<TimeSeriePoint> points;

	@OneToOne
	@JoinColumn(name=COL_CURRENT_VALUE_FK)
	private TimeSeriePoint currentValue;

	@Column(name=COL_INFLUXDB_MEASUREMENT)
	private String influxdbMeasurement;

	public TimeSerie() {
		super();
	}

	public TimeSerie(Thing thing, TimeSerieType type, String sensor) {
		super();
		this.thing = thing;
		this.type = type;
		this.sensor = sensor;
	}

	public TimeSerie(DigitalTwin twin, TimeSerieType type) {
		super();
		this.twin= twin;
		this.type = type;
	}

	public Thing getThing() {
		return thing;
	}

	public void setThing(Thing thing) {
		this.thing = thing;
	}

	public TimeSerieType getType() {
		return type;
	}

	public void setType(TimeSerieType type) {
		this.type = type;
	}

	public String getSensor() {
		return sensor;
	}

	public void setSensor(String sensor) {
		this.sensor = sensor;
	}

	public List<TimeSeriePoint> getPoints() {
		return points;
	}

	public void setPoints(List<TimeSeriePoint> points) {
		this.points = points;
	}

	public DigitalTwin getTwin() {
		return twin;
	}

	public void setTwin(DigitalTwin twin) {
		this.twin = twin;
	}

	public TimeSeriePoint getCurrentValue() {
		return currentValue;
	}

	public void setCurrentValue(TimeSeriePoint currentValue) {
		this.currentValue = currentValue;
	}

	public String getInfluxdbMeasurement() {
		return influxdbMeasurement;
	}

	public void setInfluxdbMeasurement(String influxdbMeasurement) {
		this.influxdbMeasurement = influxdbMeasurement;
	}
}
