package com.lasrosas.iot.ingestor.usecases.handleLorawanMessages.thingDrivers.adeunis;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import com.lasrosas.iot.ingestor.usecases.handleLorawanMessages.thingDrivers.adeunis.AdeunisARF8170BAFrame.*;

@Getter
@Setter
@SuperBuilder
public class Adeunis8170BAConfiguration {

	@Builder.Default
	private ChannelType channel1Type = ChannelType.DEACTIVTED;

	@Builder.Default
	private ChannelType channel2Type = ChannelType.DEACTIVTED;;

	@Builder.Default
	private ChannelType channel3Type = ChannelType.DEACTIVTED;;

	@Builder.Default
	private ChannelType channel4Type = ChannelType.DEACTIVTED;
}
