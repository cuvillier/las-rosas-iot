package com.lasrosas.iot.alarm.database.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.lasrosas.iot.core.database.entities.shared.BaseEntity;

@Entity
@Table(name = AlarmType.TABLE)
@AttributeOverrides({
		@AttributeOverride(column = @Column(name = AlarmType.COL_TECHID), name = BaseEntity.PROP_TECHID), })
public class AlarmType extends BaseEntity {
	public static final String TABLE = "t_alr_alarm_type";
	public static final String PREFIX = "alt_";
	public static final String PREFIX_FK = PREFIX + "fk_";

	public static final String COL_TECHID = PREFIX + "techid";
	public static final String COL_DATA_TYPE= PREFIX + "data_type";
	public static final String COL_CAUSE = PREFIX + "cause";

	@OneToMany(mappedBy = Alarm.PROP_TYPE, fetch = FetchType.LAZY)
	private List<Alarm> alarms;

	@Column(name = COL_CAUSE, nullable = false)
	private String cause;

	@Column(name = COL_DATA_TYPE, nullable = false)
	private String dataType;

	public AlarmType() {
	}

	public AlarmType(String dataType, String cause) {
		super();
		this.cause = cause;
		this.dataType = dataType;
	}

	public List<Alarm> getAlarms() {
		if (alarms == null)
			alarms = new ArrayList<>();
		return alarms;
	}

	public void setThings(List<Alarm> alarms) {
		this.alarms = alarms;
	}

	public String getCause() {
		return cause;
	}

	public void setCause(String cause) {
		this.cause = cause;
	}

	public void setAlarms(List<Alarm> alarms) {
		this.alarms = alarms;
	}

	public String getDataType() {
		return dataType;
	}

	public void setDataType(String datatype) {
		this.dataType = datatype;
	}
}
