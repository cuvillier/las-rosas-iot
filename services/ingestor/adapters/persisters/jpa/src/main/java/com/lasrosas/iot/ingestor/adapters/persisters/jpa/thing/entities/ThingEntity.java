package com.lasrosas.iot.ingestor.adapters.persisters.jpa.thing.entities;

import com.lasrosas.iot.ingestor.adapters.persisters.jpa.shared.LongEntity;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;

@Entity
@Table(name = ThingEntity.TABLE)
@AttributeOverrides({ @AttributeOverride(column = @Column(name = ThingEntity.COL_TECHID), name = LongEntity.PROP_TECHID)})
@Getter
@Setter
@SuperBuilder
@Slf4j
@NoArgsConstructor
public class ThingEntity extends LongEntity {

	public static final String TABLE = "t_thg_thing";
	public static final String PREFIX = "thg_";
	public static final String PREFIX_FK = PREFIX + "fk_";

	public static final String COL_TECHID = PREFIX + "techid";
	public static final String COL_READABLE = PREFIX + "readable";
	public static final String COL_NATURALID  = PREFIX + "naturalid";
	public static final String COL_DISCOVERABLE = PREFIX + "discoverable";
	public static final String COL_MODE = PREFIX + "admin_state";
	public static final String COL_CONNECTION_TIMEOUT = PREFIX + "connection_timeout";
	public static final String COL_TYPE_FK = PREFIX_FK + ThingTypeEntity.PREFIX + "type";
	public static final String COL_GATEWAY_FK = PREFIX_FK + ThingGatewayEntity.PREFIX + "gateway";

	public static final String PROP_TYPE = "type";
	public static final String PROP_GATEWAY = "gateway";
	public static final String PROP_TWIN = "twin";

	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	@JoinColumn(name = COL_GATEWAY_FK)
	private ThingGatewayEntity gateway;

	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	@JoinColumn(name = COL_TYPE_FK)
	private ThingTypeEntity type;

	@Column(name = COL_NATURALID, length = 32)
	private String naturalid;

	@Column(name = COL_READABLE, length = 64)
	private String readable;

	@Column(name = COL_CONNECTION_TIMEOUT)
	private Integer connectionTimeout;

	@Column(name = COL_DISCOVERABLE, nullable = false)
	@Builder.Default
	private boolean discoverable = false;

	@OneToOne(mappedBy = ThingProxyEntity.PROP_THING, cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private ThingProxyEntity proxy;

	public enum AdminState {
		CONNECTED,
		DISCONNECTED,
		DISABLED,
		REMOVED
	}

	@Column(name = COL_MODE)
	@Enumerated(EnumType.STRING)
	@Builder.Default
	private AdminState adminState = AdminState.DISCONNECTED;

	public boolean needToDisconnect() {

		// Check if the thing was never connected
		if(	this.connectionTimeout == null ) return false;
		if( this.proxy == null ) return false;
		if( this.proxy.getLastSeen() == null )
			return this.proxy.isConnected();	// Weired, should be disconnected.

		var lastSeen = this.proxy.getLastSeen();
		var connectedUpTo = lastSeen.plusSeconds(this.connectionTimeout);

		var shouldBeConnected = connectedUpTo.isBefore(LocalDateTime.now());

		return this.proxy.isConnected() && shouldBeConnected;
	}
}
