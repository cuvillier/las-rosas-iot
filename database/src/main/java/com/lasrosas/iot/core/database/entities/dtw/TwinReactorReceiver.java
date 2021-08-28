package com.lasrosas.iot.core.database.entities.dtw;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.lasrosas.iot.core.database.entities.shared.BaseEntity;

@Entity
@Table(name = TwinReactorReceiver.TABLE)
@AttributeOverrides({ @AttributeOverride(column = @Column(name = TwinReactorReceiver.COL_TECHID), name = BaseEntity.PROP_TECHID), })
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = TwinReactorReceiver.COL_DISCRIMINATOR)
public abstract class TwinReactorReceiver extends BaseEntity {
	public static final String TABLE = "t_dtw_reactor_receiver";
	public static final String PREFIX = "rre_";
	public static final String PREFIX_FK = PREFIX + "fk_";

	public static final String COL_TECHID = PREFIX + "techid";
	public static final String COL_DISCRIMINATOR = PREFIX + "discriminator";
	public static final String COL_TWIN_FK = PREFIX_FK + DigitalTwin.PREFIX + "twin";
	public static final String COL_TYPE_FK = PREFIX_FK + TwinReactorReceiverType.PREFIX + "type";
	public static final String COL_SOURCE_FK = PREFIX_FK + "source";

	public static final String PROP_TYPE = "type";
	public static final String PROP_TWIN = "twin";

	@ManyToOne
	@JoinColumn(name=COL_TWIN_FK)
	private DigitalTwin twin;

	@ManyToOne
	@JoinColumn(name=COL_TYPE_FK)
	private TwinReactorReceiverType type;

	public DigitalTwin getTwin() {
		return twin;
	}

	public void setTwin(DigitalTwin twin) {
		this.twin = twin;
	}

	public TwinReactorReceiverType getType() {
		return type;
	}

	public void setType(TwinReactorReceiverType type) {
		this.type = type;
	}
}
