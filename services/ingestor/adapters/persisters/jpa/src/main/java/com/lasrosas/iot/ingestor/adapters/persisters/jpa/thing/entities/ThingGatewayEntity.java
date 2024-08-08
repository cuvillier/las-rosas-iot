package com.lasrosas.iot.ingestor.adapters.persisters.jpa.thing.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.lasrosas.iot.ingestor.adapters.persisters.jpa.shared.LongEntity;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.annotations.NaturalId;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = ThingGatewayEntity.TABLE)
@AttributeOverrides({
		@AttributeOverride(column = @Column(name = ThingGatewayEntity.COL_TECHID), name = LongEntity.PROP_TECHID), })
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@Slf4j
public class ThingGatewayEntity extends LongEntity {
	public static final String TABLE = "t_thg_gateway";
	public static final String PREFIX = "gtw_";
	public static final String PREFIX_FK = PREFIX + "fk_";

	public static final String COL_TECHID = PREFIX + "techid";
	public static final String COL_NATURALID = PREFIX + "naturalid";
	public static final String COL_TYPE = PREFIX + "type";
	public static final String COL_DRIVER = PREFIX + "driver";

	public static final String PROP_TYPE = "things";

	@NaturalId
	@Column(name = COL_NATURALID, length = 32)
	private String naturalid;

	@Column(name = COL_TYPE, length = 32)
	private String type;

	@Column(name = COL_DRIVER, length = 32)
	private String driver;

	@OneToMany(mappedBy = ThingEntity.PROP_GATEWAY, fetch = FetchType.EAGER)
	@Builder.Default
	@JsonBackReference
	private List<ThingEntity> things = new ArrayList<>();
}
