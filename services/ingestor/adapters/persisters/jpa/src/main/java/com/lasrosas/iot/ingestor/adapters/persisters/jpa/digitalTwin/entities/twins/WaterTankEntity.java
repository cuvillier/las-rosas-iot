package com.lasrosas.iot.ingestor.adapters.persisters.jpa.digitalTwin.entities.twins;

import com.lasrosas.iot.ingestor.adapters.persisters.jpa.digitalTwin.entities.DigitalTwinEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Entity
@Table(name= WaterTankEntity.TABLE)
@PrimaryKeyJoinColumn(name= WaterTankEntity.COL_TECHID)
@DiscriminatorValue(WaterTankEntity.DISCRIMINATOR)
@SuperBuilder
@Getter
@Setter
public class WaterTankEntity extends DigitalTwinEntity {
	public static final Logger log = LoggerFactory.getLogger(WaterTankEntity.class);
	
	public static final String TABLE = "t_dtw_water_tank";
	public static final String PREFIX = "wat_";
	public static final String DISCRIMINATOR = "wat";

	public static final String COL_LENGTH = PREFIX + "length";
	public static final String COL_RADIUS = PREFIX + "radius";
	public static final String COL_LEVEL = PREFIX + "level";
	public static final String COL_VOLUME = PREFIX + "volume";
	public static final String COL_UPDATE_TIME = PREFIX + "update_time";
	public static final String COL_PERCENTAGE = PREFIX + "percentage";
	public static final String COL_SENSOR_ALT = PREFIX + "sensor_alt";
	public static final String COL_WATER_FLOW = PREFIX + "water_flow";
	public static final String COL_MAX_WATER_FLOW = PREFIX + "max_water_flow";
	public static final String COL_STATUS = PREFIX + "status";
	public static final String COL_TEMPERATURE = PREFIX + "temperature";
	public static final String COL_HUMIDITY = PREFIX + "humidity";

	@Column(name=COL_UPDATE_TIME)
	private LocalDateTime updateTime;

	@Column(name=COL_LENGTH)
	private Double length;

	@Column(name=COL_RADIUS)
	private Double radius;

	@Column(name=COL_SENSOR_ALT)
	private Double sensorAltitude;

	@Column(name=COL_LEVEL)
	private Double level;

	@Column(name=COL_VOLUME)
	private Double volume;

	@Column(name=COL_PERCENTAGE)
	private Double percentageFill;

	@Column(name=COL_WATER_FLOW)
	private Double waterFlow;

	@Column(name=COL_MAX_WATER_FLOW)
	private Double maxWaterFlow;

	@Column(name=COL_HUMIDITY)
	private Double humidity;

	@Column(name=COL_TEMPERATURE)
	private Double temperature;

	@Column(name=COL_STATUS)
	@Enumerated(EnumType.STRING)
	@Builder.Default
	private WaterTankEntityStatus status = WaterTankEntityStatus.UNKNOWN;
}
