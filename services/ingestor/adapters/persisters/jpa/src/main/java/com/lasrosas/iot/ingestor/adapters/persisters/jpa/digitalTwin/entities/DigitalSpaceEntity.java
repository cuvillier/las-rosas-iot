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
@Table(name = DigitalSpaceEntity.TABLE)
@AttributeOverrides({ @AttributeOverride(column = @Column(name = DigitalSpaceEntity.COL_TECHID), name = LongEntity.PROP_TECHID)})
@Getter
@Setter
@SuperBuilder
public class DigitalSpaceEntity extends LongEntity {
	public static final String TABLE = "t_dtw_space";
	public static final String PREFIX = "spa_";
	public static final String PREFIX_FK = PREFIX + "fk_";

	public static final String COL_TECHID = PREFIX + "techid";
	public static final String COL_NAME = PREFIX + "name";

	@Column(name=COL_NAME)
	private String name;

	@OneToMany(mappedBy = DigitalTwinTypeEntity.PROP_SPACE)
	@Builder.Default
	private List<DigitalTwinTypeEntity> digitalTwinTypes = new ArrayList<>();
}
