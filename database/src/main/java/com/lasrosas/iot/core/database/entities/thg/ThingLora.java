package com.lasrosas.iot.core.database.entities.thg;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue(ThingLora.DISCRIMINATOR)
public class ThingLora extends Thing {
	public static final String KIND = "lora";
	public static final String PREFIX = "lor_";
	public static final String DISCRIMINATOR = "lor";

	public static final String COL_DEVEUI = PREFIX + "dev_eui";

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

	@Override
	public String getNaturalId() {
		return this.deveui;
	}

	@Override
	public String getKind() {
		return KIND;
	}
}
