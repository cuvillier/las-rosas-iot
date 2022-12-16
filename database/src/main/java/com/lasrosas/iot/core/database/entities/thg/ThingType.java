package com.lasrosas.iot.core.database.entities.thg;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.lasrosas.iot.core.database.entities.shared.BaseEntity;

@Entity
@Table(name = ThingType.TABLE)
@AttributeOverrides({
		@AttributeOverride(column = @Column(name = ThingType.COL_TECHID), name = BaseEntity.PROP_TECHID), })
public class ThingType extends BaseEntity {
	public static final String TABLE = "t_thg_thing_type";
	public static final String PREFIX = "tty_";
	public static final String PREFIX_FK = PREFIX + "fk_";

	public static final String COL_TECHID = PREFIX + "techid";
	public static final String COL_READABLE = PREFIX + "readable";
	public static final String COL_MODEL = PREFIX + "model";
	public static final String COL_BATTERY_MIN_PERCENTAGE = PREFIX + "battery_min_percentage";
	public static final String COL_MANUFACTURER = PREFIX + "manufacturer";

	@JsonBackReference
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

	public ThingType() {
	}

	public ThingType(String manufacturer, String model) {
		super();
		this.manufacturer = manufacturer;
		this.model = model;
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
