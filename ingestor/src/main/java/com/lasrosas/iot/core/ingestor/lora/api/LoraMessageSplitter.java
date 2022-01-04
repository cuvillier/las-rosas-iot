package com.lasrosas.iot.core.ingestor.lora.api;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.splitter.AbstractMessageSplitter;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;

import com.lasrosas.iot.core.database.repo.ThingLoraRepo;
import com.lasrosas.iot.core.shared.telemetry.StillAlive;
import com.lasrosas.iot.core.shared.utils.LasRosasHeaders;

public class LoraMessageSplitter  extends AbstractMessageSplitter {

	@Autowired(required = true)
	private ThingLoraRepo thingLoraRepo;

	private final LoraService service;

	public LoraMessageSplitter(LoraService service) {
		this.service = service;
	}

	@Override
	public Object splitMessage(Message<?> imessage) {
		var result = new ArrayList<Message<?>>();
		var payload = (LoraMessage)imessage.getPayload();

		if(payload instanceof LoraMessageUplink ) {
			var thing = thingLoraRepo.getByDeveui(payload.getDeveui());

			@SuppressWarnings("unchecked")
			var splitResult = service.splitUplink((Message<LoraMessageUplink>)imessage);

			result.add(splitResult.getLoraMetricMessage());
			result.add(splitResult.getThingEncodedMessage());
			result.add(MessageBuilder
						.withPayload(new StillAlive())
						.copyHeaders(imessage.getHeaders())
						.setHeader(LasRosasHeaders.THING_ID, thing.getTechid())
						.setHeader(LasRosasHeaders.THING_NATURAL_ID, "LOR" + thing.getDeveui())
						.build()
						);

		} else if(payload instanceof LoraMessageJoin ) {

			@SuppressWarnings("unchecked")
			var splitResult = service.splitJoin((Message<LoraMessageJoin>)imessage);
			result.add(splitResult);

		} else {
			result.add(MessageBuilder
					.withPayload(new StillAlive())
					.copyHeaders(imessage.getHeaders())
					.build()
					);
		}

		return result;
	}

	public LoraService getService() {
		return service;
	}
}
