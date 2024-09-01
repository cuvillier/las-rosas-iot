package com.lasrosas.iot.ingestor.adapters.persisters.jpa.digitalTwin.entities;

import com.lasrosas.iot.ingestor.adapters.persisters.jpa.shared.LongEntity;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = ReactorReceiverTypeEntity.TABLE)
@AttributeOverrides({ @AttributeOverride(column = @Column(name = ReactorReceiverTypeEntity.COL_TECHID), name = LongEntity.PROP_TECHID)})
@Getter
@Setter
@SuperBuilder
public class ReactorReceiverTypeEntity extends LongEntity {
	public static final String TABLE = "t_dtw_reactor_receiver_type";
	public static final String PREFIX = "rrt_";
	public static final String PREFIX_FK = PREFIX + "fk_";

	public static final String COL_TECHID = PREFIX + "techid";
	public static final String COL_ROLE = PREFIX + "role";
	public static final String COL_SCHEMA = PREFIX + "schema";
	public static final String COL_REACTOR_TYPE_FK = PREFIX_FK + ReactorTypeEntity.PREFIX + "reactor_type";

	public static final String PROP_REACTOR_TYPE = "reactorType";

	@Column(name=COL_ROLE)
	private String role;

	@Column(name=COL_SCHEMA, nullable = true)
	private String schema;

	@ManyToOne(optional = true)
	@JoinColumn(name = COL_REACTOR_TYPE_FK)
	private ReactorTypeEntity reactorType;

	@OneToMany(mappedBy = ReactorReceiverEntity.PROP_TYPE)
	@Builder.Default
	private List<ReactorReceiverEntity> receivers = new ArrayList<>();
}
