package com.lasrosas.iot.core.database.entities.dtw;

import java.util.List;
import java.util.Optional;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.lasrosas.iot.core.database.entities.shared.BaseEntity;

@Entity
@Table(name = TwinReactorReceiverType.TABLE)
@AttributeOverrides({ @AttributeOverride(column = @Column(name = TwinReactorReceiverType.COL_TECHID), name = BaseEntity.PROP_TECHID)})
public class TwinReactorReceiverType extends BaseEntity {
	public static final String TABLE = "t_dtw_reactor_receiver_type";
	public static final String PREFIX = "rrt_";
	public static final String PREFIX_FK = PREFIX + "fk_";

	public static final String COL_TECHID = PREFIX + "techid";
	public static final String COL_ROLE = PREFIX + "role";
	public static final String COL_SCHEMA = PREFIX + "schema";
	public static final String COL_REACTOR_TYPE_FK = PREFIX_FK + TwinReactorType.PREFIX + "reactor_type";

	public static final String PROP_REACTOR_TYPE = "reactorType";

	@Column(name=COL_ROLE)
	private String role;

	@Column(name=COL_SCHEMA, nullable = true)
	private String schema;

	@ManyToOne(optional = true)
	@JoinColumn(name = COL_REACTOR_TYPE_FK)
	private TwinReactorType reactorType;

	@OneToMany(mappedBy = TwinReactorReceiver.PROP_TYPE)
	private List<TwinReactorReceiver> receivers;

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public Optional<String> getSchema() {
		return Optional.ofNullable(schema);
	}

	public void setSchema(Optional<String> schema) {
		this.schema = schema.orElse(null);
	}

	public TwinReactorType getReactorType() {
		return reactorType;
	}

	public void setReactorType(TwinReactorType reactorType) {
		this.reactorType = reactorType;
	}

	public List<TwinReactorReceiver> getReceivers() {
		return receivers;
	}

	public void setReceivers(List<TwinReactorReceiver> receivers) {
		this.receivers = receivers;
	}
}
