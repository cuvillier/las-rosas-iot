package com.lasrosas.iot.reactore.reactores;

public class Receiver {
	private String channel;
	private String type;

	public Receiver(String channel, String type) {
		super();
		this.channel = channel;
		this.type = type;
	}
	public String getChannel() {
		return channel;
	}
	public void setChannel(String channel) {
		this.channel = channel;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}

}
