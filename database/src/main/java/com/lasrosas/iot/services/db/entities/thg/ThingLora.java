package com.lasrosas.iot.services.db.entities.thg;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue(ThingLora.DISCRIMINATOR)
public class ThingLora extends Thing {
	public static final String PREFIX = "LOR_";
	public static final String DISCRIMINATOR = "LOR";

	public static final String COL_DEVEUI = PREFIX + "DEV_EUI";

	ThingLora() {
	}

	public ThingLora(ThingGateway gateway, ThingType type, String deveui) {
		super(gateway, type);
		this.deveui = deveui;
	}

	@Column(name = COL_DEVEUI)
	private String deveui;

	public String getDeveui() {
		return deveui;
	}

	public void setDeveui(String deveui) {
		this.deveui = deveui;
	}
	public boolean isLogLoraMessages() {
		return true;
	}
	public boolean isLogDecodedMessages() {
		return true;
	}
}
