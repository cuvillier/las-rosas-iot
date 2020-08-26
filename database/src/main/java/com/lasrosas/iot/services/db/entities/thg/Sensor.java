package com.lasrosas.iot.services.db.entities.thg;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.lasrosas.iot.services.db.entities.shared.BaseEntity;

@Entity
@Table(name=Sensor.TABLE)
@AttributeOverrides({
	@AttributeOverride(column=@Column(name=Sensor.COL_TECHID), name=BaseEntity.PROP_TECHID),
})
@DiscriminatorColumn(name=Thing.COL_DISCRIMINATOR)
public class Sensor extends BaseEntity {
	public static final String TABLE = "T_THG_SENSOR";
	public static final String PREFIX = "SEN_";
	public static final String PREFIX_FK  = PREFIX + "FK_";

	public static final String COL_THING_FK = PREFIX_FK + Thing.PREFIX + "THING";
	public static final String COL_TECHID = PREFIX + "TECHID";

	public static final String PROP_THING = "thing";

	@ManyToOne(optional=false, fetch=FetchType.LAZY)
	@JoinColumn(name=COL_THING_FK)
	private Thing thing;
}
