package com.lasrosas.iot.core.database.entities.alrm;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.lasrosas.iot.core.database.entities.shared.BaseEntity;

@Entity
@Table(name = AlarmType.TABLE)
@AttributeOverrides({ @AttributeOverride(column = @Column(name = AlarmType.COL_TECHID), name = BaseEntity.PROP_TECHID)})
public class AlarmType extends BaseEntity {
	public static final String TABLE = "t_alr_alarm_type";
	public static final String PREFIX = "alt_";
	public static final String PREFIX_FK = PREFIX + "fk_";

	public static final String COL_TECHID = PREFIX + "techid";
	public static final String COL_NAME = PREFIX + "name";

	public static final String THING_BATTERY_ALARM = "thing.battery";
	public static final String THING_CONNECTION_ALARM = "thing.connection";

	@Column(name=COL_NAME)
	private String name;

	@OneToMany(mappedBy = Alarm.PROP_TYPE)
	private List<Alarm> alarms = new ArrayList<Alarm>();

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public List<Alarm> getAlarms() {
		return alarms;
	}
	public void setAlarms(List<Alarm> alarms) {
		this.alarms = alarms;
	}
}
