package com.lasrosas.iot.alarm.database.entity;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lasrosas.iot.core.database.entities.shared.BaseEntity;

@Entity
@AttributeOverrides({ @AttributeOverride(column = @Column(name = TelemetryWatcher.COL_TECHID), name = BaseEntity.PROP_TECHID)})
@Table(name = TelemetryWatcher.TABLE)
public class TelemetryWatcher extends BaseEntity {
	public static final Logger log = LoggerFactory.getLogger(Alarm.class);

	public static final String TABLE = "t_alr_watcher";
	public static final String PREFIX = "wat_";
	public static final String PREFIX_FK = PREFIX + "fk_";

	public static final String COL_TECHID = PREFIX + "techid";
	public static final String COL_DATA_BASE_TYPE = PREFIX + "dataBaseType";
	public static final String COL_DATA_TYPE = PREFIX + "dataType";
	public static final String COL_DATA_NATURAL_ID = PREFIX + "dataNaturalId";
	public static final String COL_SCHEMA = PREFIX + "schema";

	public static final String COL_TRIGGER_FIELD = PREFIX + "triggerField";
	public static final String COL_TRIGGER_OPERATOR = PREFIX + "triggerOperator";
	public static final String COL_TRIGGER_VALUE = PREFIX + "triggerValue";
	public static final String COL_TRIGGER_GRAVITY = PREFIX + "gravity";

	@Column(name=COL_DATA_BASE_TYPE)
	private String dataBaseType;

	@Column(name=COL_DATA_TYPE)
	private String dataType;

	@Column(name=COL_DATA_NATURAL_ID)
	private String dataNaturalId;

	@Column(name=COL_SCHEMA)
	private String schema;

	@Column(name=COL_TRIGGER_FIELD)
	private String triggerField;

	@Column(name=COL_TRIGGER_OPERATOR)
	@Enumerated(EnumType.STRING)
	private TriggerOperator triggerOperator;

	@Column(name=COL_TRIGGER_VALUE)
	private Double triggerValue;

	@Column(name=COL_TRIGGER_GRAVITY)
	@Enumerated(EnumType.STRING)
	private AlarmGravity gravity;

	public String getDataBaseType() {
		return dataBaseType;
	}
	public void setDataBaseType(String dataBaseType) {
		this.dataBaseType = dataBaseType;
	}
	public String getDataType() {
		return dataType;
	}
	public void setDataType(String dataType) {
		this.dataType = dataType;
	}
	public String getDataNaturalId() {
		return dataNaturalId;
	}
	public void setDataNaturalId(String dataNaturalId) {
		this.dataNaturalId = dataNaturalId;
	}
	public String getSchema() {
		return schema;
	}
	public void setSchema(String schema) {
		this.schema = schema;
	}
	public String getTriggerField() {
		return triggerField;
	}
	public void setTriggerField(String triggerField) {
		this.triggerField = triggerField;
	}
	public TriggerOperator getTriggerOperator() {
		return triggerOperator;
	}
	public void setTriggerOperator(TriggerOperator triggerOperator) {
		this.triggerOperator = triggerOperator;
	}
	public Double getTriggerValue() {
		return triggerValue;
	}
	public void setTriggerValue(Double triggerValue) {
		this.triggerValue = triggerValue;
	}
	public AlarmGravity getGravity() {
		return gravity;
	}
	public void setGravity(AlarmGravity gravity) {
		this.gravity = gravity;
	}
	public String getAlarmType() {
		return triggerField + " " + triggerOperator.getReadable() + " " + triggerValue;
	}
	public String getAlarmMessage() {
		return schema + "." + triggerField + " " + triggerOperator.getReadable() + " " + triggerValue;
	}

	public String getType() {
		return "Watcher " + getTechid();
	}
}
