package com.lasrosas.iot.core.database.twins;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lasrosas.iot.core.database.entities.dtw.DigitalTwin;

@Entity
@Table(name=Fridge.TABLE)
@PrimaryKeyJoinColumn(name=Fridge.COL_TECHID)
@DiscriminatorValue(Fridge.DISCRIMINATOR)
public class Fridge extends DigitalTwin {
	public static final Logger log = LoggerFactory.getLogger(Fridge.class);

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
	public static final String COL_OUTSIDE_TEMP = PREFIX + "inside_temp";

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

	public Fridge() {
	}

	public Fridge(Double length, Double width, Double height, Double insideTempMax, Double insideTempMin) {
		super();
		this.length = length;
		this.width = width;
		this.height = height;
		this.insideTempMax = insideTempMax;
		this.insideTempMin = insideTempMin;
	}

	public Double getVolume() {
		if( length != null && width != null && height != null)
			return length * width * height;
		return null;
	}

	public Double getLength() {
		return length;
	}

	public void setLength(Double length) {
		this.length = length;
	}

	public Double getWidth() {
		return width;
	}

	public void setWidth(Double width) {
		this.width = width;
	}

	public Double getHeight() {
		return height;
	}

	public void setHeight(Double height) {
		this.height = height;
	}

	public Double getInsideTempMax() {
		return insideTempMax;
	}

	public void setInsideTempMax(Double insideTempMax) {
		this.insideTempMax = insideTempMax;
	}

	public Double getInsideTempMin() {
		return insideTempMin;
	}

	public void setInsideTempMin(Double insideTempMin) {
		this.insideTempMin = insideTempMin;
	}

	public Double getInsideTemp() {
		return insideTemp;
	}

	public void setInsideTemp(Double insideTemp) {
		this.insideTemp = insideTemp;
	}

	public Double getOutsideTemp() {
		return outsideTemp;
	}

	public void setOutsideTemp(Double outsideTemp) {
		this.outsideTemp = outsideTemp;
	}

	public Double getInsideHumidity() {
		return insideHumidity;
	}

	public void setInsideHumidity(Double insideHumidity) {
		this.insideHumidity = insideHumidity;
	}
}
