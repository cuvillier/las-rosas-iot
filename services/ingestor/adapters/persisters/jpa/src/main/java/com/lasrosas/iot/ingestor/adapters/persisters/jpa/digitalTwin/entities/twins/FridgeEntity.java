package com.lasrosas.iot.ingestor.adapters.persisters.jpa.digitalTwin.entities.twins;

import com.lasrosas.iot.ingestor.adapters.persisters.jpa.digitalTwin.entities.DigitalTwinEntity;
import lombok.experimental.SuperBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.persistence.*;

@Entity
@Table(name= FridgeEntity.TABLE)
@PrimaryKeyJoinColumn(name= FridgeEntity.COL_TECHID)
@DiscriminatorValue(FridgeEntity.DISCRIMINATOR)
@SuperBuilder
public class FridgeEntity extends DigitalTwinEntity {
	public static final Logger log = LoggerFactory.getLogger(FridgeEntity.class);

	public static final String TABLE = "t_dtw_fridge";
	public static final String PREFIX = "fri_";
	public static final String DISCRIMINATOR = "fri";

	public static final String COL_LENGTH = PREFIX + "length";
	public static final String COL_WIDTH = PREFIX + "width";
	public static final String COL_HEIGHT = PREFIX + "height";
	public static final String COL_INSIDE_HUMIDITY = PREFIX + "inside_humidity";
	public static final String COL_INSIDE_TEMP = PREFIX + "inside_temp";
	public static final String COL_INSIDE_TEMP_MIN = PREFIX + "inside_temp_min";
	public static final String COL_INSIDE_TEMP_MAX = PREFIX + "inside_temp_max";
	public static final String COL_OUTSIDE_TEMP = PREFIX + "outside_temp";
	public static final String COL_STATUS = PREFIX + "status";

	@Column(name=COL_LENGTH)
	private Double length;

	@Column(name=COL_WIDTH)
	private Double width;

	@Column(name=COL_HEIGHT)
	private Double height;

	@Column(name=COL_INSIDE_HUMIDITY)
	private Double insideHumidity;

	@Column(name=COL_INSIDE_TEMP_MAX)
	private Double insideTempMax;

	@Column(name=COL_INSIDE_TEMP_MIN)
	private Double insideTempMin;

	@Column(name=COL_INSIDE_TEMP)
	private Double insideTemp;

	@Column(name=COL_OUTSIDE_TEMP)
	private Double outsideTemp;

	@Column(name=COL_STATUS)
	@Enumerated(EnumType.STRING)
	private FridgeEntityStatus status;
}
