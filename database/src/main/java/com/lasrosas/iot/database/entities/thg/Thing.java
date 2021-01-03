package com.lasrosas.iot.database.entities.thg;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.lasrosas.iot.database.entities.alrm.Alarm;
import com.lasrosas.iot.database.entities.alrm.ThingAlarm;
import com.lasrosas.iot.database.entities.dtw.DigitalTwin;
import com.lasrosas.iot.database.entities.shared.BaseEntity;
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
	public static final String COL_MODE = PREFIX + "MODE";
	public static final String COL_TYPE_FK = PREFIX_FK + ThingType.PREFIX + "TYPE";
	public static final String COL_GATEWAY_FK = PREFIX_FK + ThingGateway.PREFIX + "GATEWAY";
	public static final String COL_TWIN_FK = PREFIX_FK + DigitalTwin.PREFIX + "TWIN";

	public static final String PROP_TYPE = "type";
	public static final String PROP_GATEWAY = "gateway";
	public static final String PROP_TWIN = "twin";

	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	@JoinColumn(name = COL_GATEWAY_FK)
	private ThingGateway gateway;

	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	@JoinColumn(name = COL_TYPE_FK)
	private ThingType type;

	@Column(name = COL_READABLE)
	private String readable;
	
	@OneToOne(mappedBy = ThingProxy.PROP_THING, cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private ThingProxy proxy;

	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	@JoinColumn(name = COL_TWIN_FK)
	private DigitalTwin twin;

	public enum Mode {
		Enabled,
		Disabled,
		Removed
	}

	@Column(name = COL_MODE)
	@Enumerated(EnumType.STRING)
	private Mode mode = Mode.Enabled;

	Thing() {
	}

	public Thing(@NotNull ThingGateway gateway, ThingType type) {
		this.type = type;
		type.getThings().add(this);

		this.gateway = gateway;
		gateway.getThings().add(this);

		type.getThings().add(this);
	}

	public abstract String getIdentifier();
	public abstract String getKind();

	public Mode getMode() {
		return mode;
	}

	public void setMode(Mode mode) {
		this.mode = mode;
	}

	public DigitalTwin getTwin() {
		return twin;
	}

	public void setTwin(DigitalTwin twin) {
		this.twin = twin;
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

	public String getReadable() {
		return readable;
	}

	public void setReadable(String readable) {
		this.readable = readable;
	}

	public ThingProxy getProxy() {
		return proxy;
	}

	public void setProxy(ThingProxy proxy) {
		this.proxy = proxy;
	}
}
