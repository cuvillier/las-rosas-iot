package com.lasrosas.iot.ingestor.adapters.persisters.timeserie.entities;

import com.lasrosas.iot.ingestor.adapters.persisters.shared.LongEntity;
import com.lasrosas.iot.ingestor.shared.JsonUtils;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.json.JSONObject;

import java.time.LocalDateTime;

@Entity
@Table(name = TimeSeriePointEntity.TABLE)
@AttributeOverrides({
	@AttributeOverride(column = @Column(name = TimeSeriePointEntity.COL_TECHID), name = LongEntity.PROP_TECHID)
})
@Setter
@Getter
@SuperBuilder
public class TimeSeriePointEntity extends LongEntity {
	public static final String TABLE = "t_tsr_point";
	public static final String PREFIX = "poi_";
	public static final String PREFIX_FK = PREFIX + "fk_";

	public static final String COL_TECHID = PREFIX + "techid";
	public static final String COL_TIME = PREFIX + "time";
	public static final String COL_VALUE = PREFIX + "value";
	public static final String COL_CORELATION_ID = PREFIX + "correlationId";
	public static final String COL_TIME_SERIE_FK = PREFIX_FK + TimeSerieEntity.PREFIX + "time_serie";

	public static final String PROP_TIME_SERIE = "timeSerie";

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name=COL_TIME_SERIE_FK)
	private TimeSerieEntity timeSerie;

	@Column(name=COL_TIME)
	private LocalDateTime time;

	@Column(name=COL_CORELATION_ID)
	private String correlationId;

	@Column(name=COL_VALUE)
	private String value;

	public JSONObject getJsonValue() {
		if(value == null) return null;
		return JsonUtils.fromJson(JSONObject.class, value);
	}
}
