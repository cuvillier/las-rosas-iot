package com.lasrosas.iot.core.database.entities.thg;

import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lasrosas.iot.core.database.entities.dtw.DigitalTwin;
import com.lasrosas.iot.core.database.entities.dtw.TwinReactorReceiverFromThing;
import com.lasrosas.iot.core.database.entities.shared.BaseEntity;
import com.sun.istack.NotNull;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@Table(name = Thing.TABLE)
@AttributeOverrides({ @AttributeOverride(column = @Column(name = Thing.COL_TECHID), name = BaseEntity.PROP_TECHID)})
@DiscriminatorColumn(name = Thing.COL_DISCRIMINATOR)
public abstract class Thing extends BaseEntity {
	public static final Logger log = LoggerFactory.getLogger(Thing.class);

	public static final String TABLE = "t_thg_thing";
	public static final String PREFIX = "thg_";
	public static final String PREFIX_FK = PREFIX + "fk_";

	public static final String COL_TECHID = PREFIX + "techid";
	public static final String COL_READABLE = PREFIX + "readable";
	public static final String COL_DISCRIMINATOR = PREFIX + "discriminator";
	public static final String COL_MODE = PREFIX + "mode";
	public static final String COL_CONNECTION_TIMEOUT = PREFIX + "connection_timeout";
	public static final String COL_TYPE_FK = PREFIX_FK + ThingType.PREFIX + "type";
	public static final String COL_GATEWAY_FK = PREFIX_FK + ThingGateway.PREFIX + "gateway";
	public static final String COL_TWIN_FK = PREFIX_FK + DigitalTwin.PREFIX + "twin";

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

	@Column(name = COL_CONNECTION_TIMEOUT)
	private Integer connectionTimeout;

	@OneToOne(mappedBy = ThingProxy.PROP_THING, cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private ThingProxy proxy;
	
	@OneToMany(mappedBy = TwinReactorReceiverFromThing.PROP_THING)
	private List<TwinReactorReceiverFromThing> receivers;
	
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

	public abstract String getNaturalId();
	public abstract String getKind();

	public Integer getConnectionTimeout() {
		return connectionTimeout;
	}

	public void setConnectionTimeout(Integer connectionTimeout) {
		this.connectionTimeout = connectionTimeout;
	}

	public Mode getMode() {
		return mode;
	}

	public void setMode(Mode mode) {
		this.mode = mode;
	}
	public List<TwinReactorReceiverFromThing> getReceivers() {
		return receivers;
	}

	public void setReceivers(List<TwinReactorReceiverFromThing> receivers) {
		this.receivers = receivers;
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

	public void createProxy() {
		this.proxy = new ThingProxy();
		this.proxy.setThing(this);		
	}

	public ThingProxy getCreateProxy(EntityManager em) {
		if (this.proxy == null) {
			createProxy();
			em.persist(this.proxy);
		}

		return proxy;
	}

	public ThingProxy getProxy() {
		return proxy;
	}

	public void setProxy(ThingProxy proxy) {
		this.proxy = proxy;
	}

	public boolean needToDisconnect() {

		if(this.connectionTimeout == null) return false;
		if( !this.proxy.isConnected()) return false;
		if( this.proxy.getLastSeen() == null ) return proxy.isConnected();

		var lastSeen = this.proxy.getLastSeen();
		var connectedUpTo = lastSeen.plusSeconds(this.connectionTimeout);

		var now = LocalDateTime.now();
		log.debug("now " + now);
		log.debug("connectedUpTo " + connectedUpTo);
		log.debug("connectedUpTo.after " + connectedUpTo.isAfter(now));
		log.debug("connectedUpTo.before" + connectedUpTo.isBefore(now));
		return (connectedUpTo.isBefore(now));
	}
}
