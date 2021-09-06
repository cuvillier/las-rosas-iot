package com.lasrosas.iot.core.database.entities.tsr;

import java.time.LocalDateTime;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.lasrosas.iot.core.database.entities.shared.BaseEntity;

@Entity
@Table(name = TimeSeriePoint.TABLE)
@AttributeOverrides({
	@AttributeOverride(column = @Column(name = TimeSeriePoint.COL_TECHID), name = BaseEntity.PROP_TECHID)
})
public class TimeSeriePoint extends BaseEntity {
	public static final String TABLE = "t_tsr_point";
	public static final String PREFIX = "poi_";
	public static final String PREFIX_FK = PREFIX + "fk_";

	public static final String COL_TECHID = PREFIX + "techid";
	public static final String COL_TIME = PREFIX + "time";
	public static final String COL_VALUE = PREFIX + "value";
	public static final String COL_TIME_SERIE_FK = PREFIX_FK + TimeSerie.PREFIX + "time_serie";

	public static final String PROP_TIME_SERIE = "timeSerie";

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name=COL_TIME_SERIE_FK)
	private TimeSerie timeSerie;

	@Column(name=COL_TIME)
	private LocalDateTime time;

	@Column(name=COL_VALUE)
	private String value;

	public TimeSeriePoint() {
		super();
	}

	public TimeSeriePoint(TimeSerie timeSerie, LocalDateTime time, String value) {
		super();
		this.timeSerie = timeSerie;
		this.time = time;
		this.value = value;
	}

	public TimeSerie getTimeSerie() {
		return timeSerie;
	}

	public void setTimeSerie(TimeSerie timeSerie) {
		this.timeSerie = timeSerie;
	}

	public LocalDateTime getTime() {
		return time;
	}

	public void setTime(LocalDateTime time) {
		this.time = time;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public JsonObject getValue(Gson gson) {
		if(value == null) return null;
		return gson.fromJson(value, JsonObject.class);
	}
}
