package com.lasrosas.iot.services.db.entities.thg;

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
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.lasrosas.iot.services.db.entities.shared.BaseEntity;
import com.sun.istack.NotNull;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@Table(name = Thing.TABLE)
@AttributeOverrides({ @AttributeOverride(column = @Column(name = Thing.COL_TECHID), name = BaseEntity.PROP_TECHID), })
@DiscriminatorColumn(name = Thing.COL_DISCRIMINATOR)
public abstract class Thing extends BaseEntity {
	public static final String TABLE = "T_THG_THING";
	public static final String PREFIX = "THG_";
	public static final String PREFIX_FK = PREFIX + "FK_";

	public static final String COL_TECHID = PREFIX + "TECHID";
	public static final String COL_READABLE = PREFIX + "READABLE";
	public static final String COL_DISCRIMINATOR = PREFIX + "DISCRIMINATOR";
	public static final String COL_TYPE_FK = PREFIX_FK + ThingType.PREFIX + "TYPE";
	public static final String COL_GATEWAY_FK = PREFIX_FK + ThingGateway.PREFIX + "GATEWAY";

	public static final String PROP_TYPE = "type";
	public static final String PROP_GATEWAY = "gateway";

	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	@JoinColumn(name = COL_GATEWAY_FK)
	private ThingGateway gateway;

	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	@JoinColumn(name = COL_TYPE_FK)
	private ThingType type;

	@Column(name = COL_READABLE)
	private String readable;
	
	@OneToOne(mappedBy = ThingProxy.PROP_THING)
	private ThingProxy proxy;

	Thing() {
	}

	public Thing(@NotNull ThingGateway gateway, ThingType type) {
		this.type = type;
		type.getThings().add(this);

		this.gateway = gateway;
		gateway.getThings().add(this);

		type.getThings().add(this);
	}

	public ThingGateway getGateway() {
		return gateway;
	}

	public void setGateway(ThingGateway gateway) {
		this.gateway = gateway;
	}

	public ThingType getType() {
		return type;
	}

	public void setType(ThingType type) {
		this.type = type;
	}
}
