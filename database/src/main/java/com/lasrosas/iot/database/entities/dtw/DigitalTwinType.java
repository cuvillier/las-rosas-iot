package com.lasrosas.iot.database.entities.dtw;

import java.util.List;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.lasrosas.iot.database.entities.shared.BaseEntity;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@Table(name = DigitalTwinType.TABLE)
@AttributeOverrides({ @AttributeOverride(column = @Column(name = DigitalTwinType.COL_TECHID), name = BaseEntity.PROP_TECHID), })
@DiscriminatorColumn(name = DigitalTwinType.COL_DISCRIMINATOR)
public abstract class DigitalTwinType extends BaseEntity {
	public static final String TABLE = "t_dtw_digital_twin_type";
	public static final String PREFIX = "twt_";
	public static final String PREFIX_FK = PREFIX + "fk_";
	public static final String COL_DISCRIMINATOR = PREFIX + "discriminator";

	public static final String COL_TECHID = PREFIX + "techid";
	public static final String COL_NAME = PREFIX + "name";
	public static final String COL_PUBLISH_ONTHOLOGY = PREFIX + "publish_onthology";
	
	public static final String COL_MAY_HAVE_CHILDREN = PREFIX + "may_have_children";
	public static final String COL_SPACE_FK = PREFIX_FK + DigitalSpace.PREFIX + "space";

	public static final String PROP_SPACE = "space";

	@ManyToOne
	@JoinColumn(name=COL_SPACE_FK)
	private DigitalSpace space;

	@OneToMany(mappedBy = DigitalTwin.PROP_TYPE)
	private List<DigitalTwin> twins;

	@Column(name=COL_MAY_HAVE_CHILDREN)
	private boolean mayHaveChildren = false;

	@Column(name=COL_NAME)
	private String name;

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public DigitalSpace getSpace() {
		return space;
	}
	public void setSpace(DigitalSpace space) {
		this.space = space;
	}
	public boolean isMayHaveChildren() {
		return mayHaveChildren;
	}
	public void setMayHaveChildren(boolean mayHaveChildren) {
		this.mayHaveChildren = mayHaveChildren;
	}
	public List<DigitalTwin> getTwins() {
		return twins;
	}
	public void setTwins(List<DigitalTwin> twins) {
		this.twins = twins;
	}
}
