package com.lasrosas.iot.alarm.database.entity;

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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lasrosas.iot.core.database.entities.shared.BaseEntity;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@Table(name = Alarm.TABLE)
@AttributeOverrides({ @AttributeOverride(column = @Column(name = Alarm.COL_TECHID), name = BaseEntity.PROP_TECHID)})
@DiscriminatorColumn(name = Alarm.COL_DISCRIMINATOR)
public abstract class Alarm extends BaseEntity {
	public static final Logger log = LoggerFactory.getLogger(Alarm.class);

	public static final String TABLE = "t_alr_alarm";
	public static final String PREFIX = "alr_";
	public static final String PREFIX_FK = PREFIX + "fk_";

	public static final String COL_TECHID = PREFIX + "techid";
	public static final String COL_DISCRIMINATOR = PREFIX + "discriminator";
	public static final String COL_STATE = PREFIX + "state";
	public static final String COL_GRAVITY = PREFIX + "gravity";
	public static final String COL_OPENED_TIME = PREFIX + "opened_time";
	public static final String COL_ACK_TIME = PREFIX + "ack_time";
	public static final String COL_CLOSED_TIME = PREFIX + "closed_time";
	public static final String COL_TYPE = PREFIX + "type";
	public static final String COL_MESSAGE = PREFIX + "message";

	public static final String PROP_TYPE = "type";

	@Column(name = COL_OPENED_TIME)
	private LocalDateTime openedTime;

	@Column(name = COL_ACK_TIME)
	private LocalDateTime ackTime;

	@Column(name = COL_TYPE)
	private String type;

	@Column(name = COL_MESSAGE)
	private String message;

	@Column(name = COL_CLOSED_TIME)
	private LocalDateTime closedTime;

	@Column(name = COL_STATE)
	@Enumerated(EnumType.STRING)
	private AlarmState state;

	@Column(name = COL_GRAVITY)
	@Enumerated(EnumType.STRING)
	private AlarmGravity gravity;

	public Alarm() {
		this.state = AlarmState.OPENED;
		this.gravity = AlarmGravity.HIGH;
	}

	public Alarm(LocalDateTime time, String type, String message, AlarmGravity gravity) {
		this();

		if(gravity == null) gravity = AlarmGravity.HIGH;

		if(time == null) time = LocalDateTime.now();
		this.openedTime = time;
		this.type = type;
		this.message = message;
	}

	public AlarmState getState() {
		return state;
	}

	public AlarmGravity getGravity() {
		return gravity;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public void setGravity(AlarmGravity gravity) {
		this.gravity = gravity;
	}

	public void setState(AlarmState state, LocalDateTime time) {
		this.state = state;

		if(time == null) time = LocalDateTime.now();

		switch(state) {
		case OPENED:
			this.openedTime = time;
			break;
		case ACKNOWLEDGE:
			this.ackTime = time;
			break;
		case CLOSED:
			this.closedTime = time;
			break;
		default:
			throw new RuntimeException("Invalid alarm state " + state + ". Fix the code");
		}
	}

	public void setState(AlarmState state) {
		this.state = state;
	}

	public LocalDateTime getOpenedTime() {
		return openedTime;
	}

	public void setOpenedTime(LocalDateTime openedTime) {
		this.openedTime = openedTime;
	}

	public LocalDateTime getAckTime() {
		return ackTime;
	}

	public void setAckTime(LocalDateTime ackTime) {
		this.ackTime = ackTime;
	}

	public LocalDateTime getClosedTime() {
		return closedTime;
	}

	public void setClosedTime(LocalDateTime closedTime) {
		this.closedTime = closedTime;
	}
	
	public abstract String getSourceNaturalId();
}
