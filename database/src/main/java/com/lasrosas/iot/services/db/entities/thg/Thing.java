package com.lasrosas.iot.services.db.entities.thg;

import java.util.List;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.lasrosas.iot.services.db.entities.shared.BaseEntity;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@Table(name=Thing.TABLE)
@AttributeOverrides({
	@AttributeOverride(column=@Column(name=Thing.COL_TECHID), name=BaseEntity.PROP_TECHID),
})
@DiscriminatorColumn(name=Thing.COL_DISCRIMINATOR)
public abstract class Thing extends BaseEntity {
	public static final String TABLE = "T_THG_THING";
	public static final String PREFIX = "THG_";
	public static final String PREFIX_FK  = PREFIX + "FK_";

	public static final String COL_TECHID = PREFIX + "TECHID";
	public static final String COL_DISCRIMINATOR = PREFIX + "DISCRIMINATOR";
	public static final String COL_TYPE_FK = PREFIX_FK + ThingType.PREFIX + "TYPE";

	public static final String PROP_TYPE = "type";

	@ManyToOne(optional=false, fetch=FetchType.LAZY)
	@JoinColumn(name=COL_TYPE_FK)
	private ThingType type;

	@OneToMany(mappedBy=Sensor.PROP_THING, fetch=FetchType.EAGER)
	private List<Sensor> sensors;

}
