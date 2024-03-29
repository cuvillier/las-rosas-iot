package com.lasrosas.iot.core.database.entities.dtw;

import java.util.List;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.lasrosas.iot.core.database.entities.shared.BaseEntity;

@Entity
@Table(name = TwinReactorType.TABLE)
@AttributeOverrides({ @AttributeOverride(column = @Column(name = TwinReactorType.COL_TECHID), name = BaseEntity.PROP_TECHID)})
public class TwinReactorType extends BaseEntity {
	public static final String TABLE = "t_dtw_reactor_type";
	public static final String PREFIX = "rat_";
	public static final String PREFIX_FK = PREFIX + "fk_";

	public static final String COL_TECHID = PREFIX + "techid";
	public static final String COL_DISCRIMINATOR = PREFIX + "discriminator";
	public static final String COL_BEAN = PREFIX + "bean";

	@Column(name=COL_BEAN)
	private String bean;

	@OneToMany(mappedBy = TwinReactorReceiverType.PROP_REACTOR_TYPE)
	private List<TwinReactorReceiverType> receiverTypes;

	public String getBean() {
		return bean;
	}

	public void setBean(String bean) {
		this.bean = bean;
	}

	public List<TwinReactorReceiverType> getReceiverTypes() {
		return receiverTypes;
	}

	public void setReceiverTypes(List<TwinReactorReceiverType> receiverTypes) {
		this.receiverTypes = receiverTypes;
	}
}
