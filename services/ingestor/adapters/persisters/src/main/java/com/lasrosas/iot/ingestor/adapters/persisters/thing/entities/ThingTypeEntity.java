package com.lasrosas.iot.ingestor.adapters.persisters.thing.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.lasrosas.iot.ingestor.adapters.persisters.shared.LongEntity;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = ThingTypeEntity.TABLE)
@AttributeOverrides({
		@AttributeOverride(column = @Column(name = ThingTypeEntity.COL_TECHID), name = LongEntity.PROP_TECHID), })
@Getter
@Setter
@SuperBuilder
@Slf4j
@NoArgsConstructor
public class ThingTypeEntity extends LongEntity {
	public static final String TABLE = "t_thg_thing_type";
	public static final String PREFIX = "tty_";
	public static final String PREFIX_FK = PREFIX + "fk_";

	public static final String COL_TECHID = PREFIX + "techid";
	public static final String COL_READABLE = PREFIX + "readable";
	public static final String COL_MODEL = PREFIX + "model";
	public static final String COL_BATTERY_MIN_PERCENTAGE = PREFIX + "battery_min_percentage";
	public static final String COL_MANUFACTURER = PREFIX + "manufacturer";

	@Column(name = COL_MANUFACTURER)
	private String manufacturer;

	@Column(name = COL_MODEL)
	private String model;

	@Column(name = COL_READABLE)
	private String readable;

	@Column(name = COL_BATTERY_MIN_PERCENTAGE)
	private Double batteryMinPercentage;

	@OneToMany(mappedBy = ThingEntity.PROP_TYPE, fetch = FetchType.LAZY)
	@Builder.Default
	@JsonBackReference
	private List<ThingEntity> things = new ArrayList<>();
}
