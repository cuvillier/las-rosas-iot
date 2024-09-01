package com.lasrosas.iot.ingestor.adapters.persisters.jpa.digitalTwin.entities.twins;

import com.lasrosas.iot.ingestor.adapters.persisters.jpa.digitalTwin.entities.DigitalTwinTypeEntity;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Entity
@DiscriminatorValue(MultiSwitchTypeEntity.DISCRIMINATOR)
@SuperBuilder
@Getter
@Setter
public class MultiSwitchTypeEntity extends DigitalTwinTypeEntity {
	public static final String DISCRIMINATOR = "mst";

	public static final String PREFIX = "mst_";
	public static final String PREFIX_FK = PREFIX + "fk_";
	public static final String COL_MAX_STATE = PREFIX + "max_state";
	public static final String COL_TECHID = PREFIX + "techid";

	@Column(name=COL_MAX_STATE)
	@Builder.Default
	private Integer maxState = 2;
}