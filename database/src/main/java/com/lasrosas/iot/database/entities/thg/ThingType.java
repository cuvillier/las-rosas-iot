package com.lasrosas.iot.database.entities.thg;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.lasrosas.iot.database.entities.shared.BaseEntity;

@Entity
@Table(name = ThingType.TABLE)
@AttributeOverrides({
		@AttributeOverride(column = @Column(name = ThingType.COL_TECHID), name = BaseEntity.PROP_TECHID), })
public class ThingType extends BaseEntity {
	public static final String TABLE = "T_THG_THING_TYPE";
	public static final String PREFIX = "TTY_";
	public static final String PREFIX_FK = PREFIX + "FK_";

	public static final String COL_TECHID = PREFIX + "TECHID";
	public static final String COL_READABLE = PREFIX + "READABLE";
	public static final String COL_MODEL = PREFIX + "MODEL";
	public static final String COL_BATTERY_MIN_PERCENTAGE = PREFIX + "BATTERY_MIN_PERCENTAGE";
	public static final String COL_MANUFACTURER = PREFIX + "MANUFACTURER";

	@OneToMany(mappedBy = Thing.PROP_TYPE, fetch = FetchType.LAZY)
	private List<Thing> things;

	@Column(name = COL_MANUFACTURER)
	private String manufacturer;

	@Column(name = COL_MODEL)
	private String model;

	@Column(name = COL_READABLE)
	private String readable;

	@Column(name = COL_BATTERY_MIN_PERCENTAGE)
	private Double batteryMinPercentage;

	ThingType() {
	}

	public ThingType(String readable) {

	}

	public List<Thing> getThings() {
		if (things == null)
			things = new ArrayList<>();
		return things;
	}

	public void setThings(List<Thing> things) {
		this.things = things;
	}

	public String getManufacturer() {
		return manufacturer;
	}

	public void setManufacturer(String manufacturer) {
		this.manufacturer = manufacturer;
	}

	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public String getReadable() {
		return readable;
	}

	public void setReadable(String readable) {
		this.readable = readable;
	}

	public Double getBatteryMinPercentage() {
		return batteryMinPercentage;
	}

	public void setBatteryMinPercentage(Double batteryMinPercentage) {
		this.batteryMinPercentage = batteryMinPercentage;
	}
}
