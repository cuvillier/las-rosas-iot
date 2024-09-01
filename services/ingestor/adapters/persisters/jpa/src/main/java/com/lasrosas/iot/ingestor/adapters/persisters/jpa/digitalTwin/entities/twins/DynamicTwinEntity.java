package com.lasrosas.iot.ingestor.adapters.persisters.jpa.digitalTwin.entities.twins;

import com.lasrosas.iot.ingestor.adapters.persisters.jpa.digitalTwin.entities.DigitalTwinEntity;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name= DynamicTwinEntity.TABLE)
@PrimaryKeyJoinColumn(name= DynamicTwinEntity.COL_TECHID)
@DiscriminatorValue(DynamicTwinEntity.DISCRIMINATOR)
@SuperBuilder
public class DynamicTwinEntity extends DigitalTwinEntity {
	public static final String TABLE = "t_dtw_dynamic_twin";
	public static final String PREFIX = "ytw_";
	public static final String DISCRIMINATOR = "dyn";
}
