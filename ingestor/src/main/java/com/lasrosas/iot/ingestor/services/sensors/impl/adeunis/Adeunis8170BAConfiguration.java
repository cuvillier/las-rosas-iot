package com.lasrosas.iot.ingestor.services.sensors.impl.adeunis;

import com.lasrosas.iot.ingestor.services.sensors.impl.adeunis.AdeunisARF8170BAFrame.ChannelType;

public class Adeunis8170BAConfiguration {

	private ChannelType channel1Type = ChannelType.DEACTIVTED;
	private ChannelType channel2Type = ChannelType.DEACTIVTED;;
	private ChannelType channel3Type = ChannelType.DEACTIVTED;;
	private ChannelType channel4Type = ChannelType.DEACTIVTED;

	public ChannelType getChannel1Type() {
		return channel1Type;
	}
	public void setChannel1Type(ChannelType channel1Type) {
		this.channel1Type = channel1Type;
	}
	public ChannelType getChannel2Type() {
		return channel2Type;
	}
	public void setChannel2Type(ChannelType channel2Type) {
		this.channel2Type = channel2Type;
	}
	public ChannelType getChannel3Type() {
		return channel3Type;
	}
	public void setChannel3Type(ChannelType channel3Type) {
		this.channel3Type = channel3Type;
	}
	public ChannelType getChannel4Type() {
		return channel4Type;
	}
	public void setChannel4Type(ChannelType channel4Type) {
		this.channel4Type = channel4Type;
	};
}
