package com.lasrosas.iot.core.database.entities.tsr;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.springframework.util.Assert;

import com.lasrosas.iot.core.database.entities.shared.BaseEntity;

@Entity
@Table(name = TimeSerieType.TABLE)
@AttributeOverrides({
		@AttributeOverride(column = @Column(name = TimeSerieType.COL_TECHID), name = BaseEntity.PROP_TECHID)
})
public class TimeSerieType extends BaseEntity {
	public static final String TABLE = "t_tsr_time_serie_type";
	public static final String PREFIX = "tst_";
	public static final String COL_TECHID = PREFIX + "techid";
	public static final String COL_SCHEMA = PREFIX + "schema";
	public static final String COL_PERSISTENT = PREFIX + "persistent";
	public static final String COL_RETENTION = PREFIX + "retention";

	@OneToMany(mappedBy = TimeSerie.PROP_TYPE)
	private List<TimeSerie> timeSeries;

	@Column(name=COL_PERSISTENT)
	private boolean persistent;

	@Column(name=COL_RETENTION)
	private Integer retention;

	@Column(name=COL_SCHEMA)
	private String schema;

	public TimeSerieType() {
	}

	public TimeSerieType(String schema) {
		this.schema = schema;
	}

	public boolean isPersistent() {
		return persistent;
	}

	public void setPersistent(boolean persistent) {
		this.persistent = persistent;
	}

	public Integer getRetention() {
		return retention;
	}

	public void setRetention(Integer retention) {
		if( retention != null) Assert.isTrue(retention >= 0, "Retention must be >= 0");
		this.retention = retention;
	}

	public List<TimeSerie> getTimeSeries() {
		if (timeSeries == null)
			timeSeries = new ArrayList<>();
		return timeSeries;
	}

	public void setTimeSeries(List<TimeSerie> timeSeries) {
		this.timeSeries = timeSeries;
	}

	public String getSchema() {
		return schema;
	}

	public void setSchema(String schema) {
		this.schema = schema;
	}
}
