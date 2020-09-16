package com.lasrosas.iot.services.db.entities.tsr;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.lasrosas.iot.services.db.entities.shared.BaseEntity;

@Entity
@Table(name = TimeSerieType.TABLE)
@AttributeOverrides({
		@AttributeOverride(column = @Column(name = TimeSerieType.COL_TECHID), name = BaseEntity.PROP_TECHID)
})
public class TimeSerieType extends BaseEntity {
	public static final String TABLE = "T_TSR_TIME_SERIE_TYPE";
	public static final String PREFIX = "TST_";
	public static final String COL_TECHID = PREFIX + "TECHID";
	public static final String COL_REDEABLE = PREFIX + "READABLE";
	public static final String COL_SCHEMA = PREFIX + "SCHEMA";

	@OneToMany(mappedBy = TimeSerie.PROP_TYPE)
	private List<TimeSerie> timeSeries;

	@Column(name=COL_REDEABLE)
	private String readable;

	@Column(name=COL_SCHEMA)
	private String schema;

	public TimeSerieType() {
		super();
	}

	public TimeSerieType(String schema) {
		super();
		this.schema = schema;
	}

	public List<TimeSerie> getTimeSeries() {
		if (timeSeries == null)
			timeSeries = new ArrayList<>();
		return timeSeries;
	}

	public void setTimeSeries(List<TimeSerie> timeSeries) {
		this.timeSeries = timeSeries;
	}

	public String getReadable() {
		return readable;
	}

	public void setReadable(String readable) {
		this.readable = readable;
	}

	public String getSchema() {
		return schema;
	}

	public void setSchema(String schema) {
		this.schema = schema;
	}
}
