package com.lasrosas.iot.ingestor.adapters.persisters.timeserie.entities;

import com.lasrosas.iot.ingestor.adapters.persisters.shared.LongEntity;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = TimeSerieTypeEntity.TABLE)
@AttributeOverrides({
		@AttributeOverride(column = @Column(name = TimeSerieTypeEntity.COL_TECHID), name = LongEntity.PROP_TECHID)
})
@Getter
@Setter
@SuperBuilder
public class TimeSerieTypeEntity extends LongEntity {
	public static final String TABLE = "t_tsr_time_serie_type";
	public static final String PREFIX = "tst_";
	public static final String COL_TECHID = PREFIX + "techid";
	public static final String COL_SCHEMA = PREFIX + "schema";

	@Builder.Default
	@OneToMany(mappedBy = TimeSerieEntity.PROP_TYPE)
	private List<TimeSerieEntity> timeSeries = new ArrayList<>();

	@Column(name=COL_SCHEMA)
	private String schema;
}
