package com.lasrosas.iot.core.database.entities.dtw;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.lasrosas.iot.core.database.entities.shared.BaseEntity;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = DigitalTwin.TABLE)
@AttributeOverrides({ @AttributeOverride(column = @Column(name = DigitalTwin.COL_TECHID), name = BaseEntity.PROP_TECHID)})
@DiscriminatorColumn(name=DigitalTwin.COL_DESCRIMINATOR)
public abstract class DigitalTwin extends BaseEntity {

	public static final String TABLE = "t_dtw_digital_twin";
	public static final String PREFIX = "twi_";
	public static final String PREFIX_FK = PREFIX + "fk_";

	public static final String COL_TECHID = PREFIX + "techid";
	public static final String COL_DESCRIMINATOR = PREFIX + "discriminator";
	public static final String COL_NAME = PREFIX + "name";
	public static final String COL_PROPERTIES = PREFIX + "properties";
	public static final String COL_TYPE_FK = PREFIX_FK + DigitalTwinType.PREFIX + "type";
	public static final String COL_PART_OF_FK = PREFIX_FK + DigitalTwin.PREFIX + "part_of";

	public static final String PROP_TYPE = "type";
	public static final String PROP_PART_OF = "partOf";

	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	@JoinColumn(name = COL_TYPE_FK)
	private DigitalTwinType type;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = COL_PART_OF_FK)
	private DigitalTwin partOf;

	@OneToMany(mappedBy = PROP_PART_OF)
	private List<DigitalTwin> parts;

	@Column(name=COL_PROPERTIES)
	private String properties;

	@Column(name=COL_NAME)
	private String name;

	@OneToMany(mappedBy = TwinReactorReceiver.PROP_TWIN)
	private List<TwinReactorReceiver> receivers = new ArrayList<>();

	public DigitalTwinType getType() {
		return type;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setType(DigitalTwinType type) {
		this.type = type;
	}

	public DigitalTwin getPartOf() {
		return partOf;
	}

	public void setPartOf(DigitalTwin partOf) {
		this.partOf = partOf;
	}

	public List<DigitalTwin> getParts() {
		return parts;
	}

	public void setParts(List<DigitalTwin> children) {
		this.parts = children;
	}

	public String getProperties() {
		return properties;
	}

	public void setProperties(String properties) {
		this.properties = properties;
	}

	public List<TwinReactorReceiver> getReceivers() {
		return receivers;
	}

	public void setReceivers(List<TwinReactorReceiver> receivers) {
		this.receivers = receivers;
	}
}
