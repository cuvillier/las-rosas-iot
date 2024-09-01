package com.lasrosas.iot.ingestor.adapters.persisters.jpa.digitalTwin.entities.twins;

import com.lasrosas.iot.ingestor.adapters.persisters.jpa.digitalTwin.entities.DigitalTwinEntity;
import lombok.Builder;
import lombok.experimental.SuperBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.persistence.*;
import java.util.Optional;

@Entity
@Table(name= MultiSwitchEntity.TABLE)
@PrimaryKeyJoinColumn(name= MultiSwitchEntity.COL_TECHID)
@DiscriminatorValue(MultiSwitchEntity.DISCRIMINATOR)
@SuperBuilder
public class MultiSwitchEntity extends DigitalTwinEntity {
	public static final Logger log = LoggerFactory.getLogger(MultiSwitchEntity.class);

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
	@Builder.Default
	private int state = OFF;

	@Column(name=COL_EXPECTED_STATE)
	@Builder.Default
	private int expectedState = OFF;

	@Column(name=COL_CONNECTED)
	@Builder.Default
	private boolean connected = false;

	@Column(name=COL_STATE_WHEN_CONNECT)
	@Builder.Default
	private Integer stateWhenConnect = OFF;
}
