package com.lasrosas.iot.core.database.entities.thg;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.NaturalId;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.lasrosas.iot.core.database.entities.shared.BaseEntity;

@Entity
@Table(name = ThingGateway.TABLE)
@AttributeOverrides({
		@AttributeOverride(column = @Column(name = ThingGateway.COL_TECHID), name = BaseEntity.PROP_TECHID), })
public class ThingGateway extends BaseEntity {
	public static final String TABLE = "t_thg_gateway";
	public static final String PREFIX = "gtw_";
	public static final String PREFIX_FK = PREFIX + "fk_";

	public static final String COL_TECHID = PREFIX + "techid";
	public static final String COL_NATURAL_ID = PREFIX + "natural_id";
	public static final String COL_PROTOCOL = PREFIX + "protocol";
	public static final String COL_URL = PREFIX + "url";
	public static final String COL_LOGIN = PREFIX + "login";
	public static final String COL_PASSWORD = PREFIX + "password";
	public static final String COL_TYPE_NAME = PREFIX + "type_name";

	public static final String PROP_TYPE = "things";

	@NaturalId
	@Column(name = COL_NATURAL_ID)
	private String naturalId;

	@OneToMany(mappedBy = Thing.PROP_GATEWAY, fetch = FetchType.EAGER)
	@JsonBackReference
	private List<Thing> things;

	@Column(name = COL_PROTOCOL)
	private String protocol;

	@Column(name = COL_LOGIN)
	private String login;

	@Column(name = COL_PASSWORD)
	private String password;

	@Column(name = COL_URL)
	private String url;

	@Column(name = COL_TYPE_NAME)
	private String typeName;

	public ThingGateway() {}

	public ThingGateway(String naturalId) {
		this.naturalId = naturalId;
	}

	public String getTypeName() {
		return typeName;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}

	public List<Thing> getThings() {
		if (things == null)
			things = new ArrayList<>();
		return things;
	}


	public void setThings(List<Thing> things) {
		this.things = things;
	}

	public String getNaturalId() {
		return naturalId;
	}

	public void setNaturalId(String naturalId) {
		this.naturalId = naturalId;
	}

	public String getProtocol() {
		return protocol;
	}

	public void setProtocol(String protocol) {
		this.protocol = protocol;
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
}
