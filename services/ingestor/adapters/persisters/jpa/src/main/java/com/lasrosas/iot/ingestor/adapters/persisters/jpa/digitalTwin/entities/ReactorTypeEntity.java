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
@Table(name = ReactorTypeEntity.TABLE)
@AttributeOverrides({ @AttributeOverride(column = @Column(name = ReactorTypeEntity.COL_TECHID), name = LongEntity.PROP_TECHID)})
@Getter
@Setter
@SuperBuilder
public class ReactorTypeEntity extends LongEntity {
	public static final String TABLE = "t_dtw_reactor_type";
	public static final String PREFIX = "rat_";
	public static final String PREFIX_FK = PREFIX + "fk_";

	public static final String COL_TECHID = PREFIX + "techid";
	public static final String COL_BEAN = PREFIX + "bean";

	@Column(name=COL_BEAN)
	private String bean;

	@OneToMany(mappedBy = ReactorReceiverTypeEntity.PROP_REACTOR_TYPE)
	@Builder.Default
	private List<ReactorReceiverTypeEntity> receiverTypes = new ArrayList<>();
}
