package com.lasrosas.iot.database.entities.alrm;

import java.time.LocalDateTime;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.lasrosas.iot.database.entities.dtw.DigitalTwinType;
import com.lasrosas.iot.database.entities.shared.BaseEntity;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@Table(name = Alarm.TABLE)
@AttributeOverrides({ @AttributeOverride(column = @Column(name = Alarm.COL_TECHID), name = BaseEntity.PROP_TECHID)})
@DiscriminatorColumn(name = Alarm.COL_DISCRIMINATOR)
public abstract class Alarm extends BaseEntity {
	public static final String TABLE = "T_ALR_ALARM";
	public static final String PREFIX = "ALR_";
	public static final String PREFIX_FK = PREFIX + "FK_";

	public static final String COL_TECHID = PREFIX + "TECHID";
	public static final String COL_TIME = PREFIX + "TIME";
	public static final String COL_STATE = PREFIX + "STATE";
	public static final String COL_OPENED_TIME = PREFIX + "OPENED_TIME";
	public static final String COL_CLOSED_TIME = PREFIX + "CLOSED_TIME";
	public static final String COL_DISCRIMINATOR = PREFIX + "DISCRIMINATOR";
	public static final String COL_TYPE_FK = PREFIX_FK + AlarmType.PREFIX + "TYPE";

	public static final String PROP_TYPE = "type";

	@ManyToOne
	@JoinColumn(name = COL_TYPE_FK)
	private AlarmType type;

	@Column(name=COL_OPENED_TIME)
	private LocalDateTime openedTime;

	@Column(name=COL_CLOSED_TIME)
	private LocalDateTime closedTime;

	public enum State {
		Opened,
		Closed
	}

	@Enumerated(EnumType.STRING)	
	@Column(name=COL_STATE)
	private State state;

	public Alarm() {
	}

	public Alarm(AlarmType type, LocalDateTime time) {
		super();
		this.type = type;
		this.openedTime = time;
		this.state = State.Opened;
	}

	public AlarmType getType() {
		return type;
	}

	public void setType(AlarmType type) {
		this.type = type;
	}

	public LocalDateTime getOpenedTime() {
		return openedTime;
	}

	public void setOpenedTime(LocalDateTime openedTime) {
		this.openedTime = openedTime;
	}

	public LocalDateTime getClosedTime() {
		return closedTime;
	}

	public void setClosedTime(LocalDateTime closedTime) {
		this.closedTime = closedTime;
	}

	public State getState() {
		return state;
	}

	public void setState(State state) {
		this.state = state;
	}

	public void close(LocalDateTime time) {
		this.closedTime = time;
		this.state = State.Closed;
	}
}
