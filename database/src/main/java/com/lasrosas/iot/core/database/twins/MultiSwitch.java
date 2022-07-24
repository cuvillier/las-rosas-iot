package com.lasrosas.iot.core.database.twins;

import java.util.Optional;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lasrosas.iot.core.database.entities.dtw.DigitalTwin;

@Entity
@Table(name=MultiSwitch.TABLE)
@PrimaryKeyJoinColumn(name=MultiSwitch.COL_TECHID)
@DiscriminatorValue(MultiSwitch.DISCRIMINATOR)
public class MultiSwitch extends DigitalTwin {
	public static final Logger log = LoggerFactory.getLogger(MultiSwitch.class);

	public static final int OFF = 0;
	public static final int ON = 1;

	public static final String TABLE = "t_dtw_multiswitch";
	public static final String PREFIX = "msw_";
	public static final String DISCRIMINATOR = "msw";
	public static final String COL_STATE = PREFIX + "state";
	public static final String COL_EXPECTED_STATE = PREFIX + "expected_state";
	public static final String COL_CONNECTED = PREFIX + "connected";
	public static final String COL_STATE_WHEN_CONNECT = PREFIX + "state_when_connect";

	@Column(name=COL_STATE)
	private int state = OFF;

	@Column(name=COL_EXPECTED_STATE)
	private int expectedState = OFF;

	@Column(name=COL_CONNECTED)
	private int connected = 0;

	@Column(name=COL_STATE_WHEN_CONNECT)
	private Integer stateWhenConnect = OFF;

	public boolean isOff() {
		return state == OFF;
	}

	public int getExpectedState() {
		return expectedState;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public boolean isConnected() {
		return connected == 1;
	}

	public int getConnected() {
		return connected;
	}

	public void setConnected(int connected) {
		this.connected = connected;
	}

	public void setExpectedState(int expectedState) {
		this.expectedState = expectedState;
	}

	public Optional<Integer> getStateWhenConnect() {
		return Optional.ofNullable(stateWhenConnect);
	}

	public void setStateWhenConnect(Optional<Integer> stateWhenConnect) {
		this.stateWhenConnect = stateWhenConnect.orElse(null);
	}
}
