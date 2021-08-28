package com.lasrosas.iot.core.reactor.base;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;

import com.lasrosas.iot.core.database.repo.ThingRepo;
import com.lasrosas.iot.core.database.repo.TwinReactorReceiverFromThingRepo;
import com.lasrosas.iot.core.reactor.api.ReactorService;
import com.lasrosas.iot.core.shared.telemetry.Telemetry;
import com.lasrosas.iot.core.shared.utils.LasRosasHeaders;

public class ReactorServiceImpl implements ReactorService {

	@Autowired
	private ThingRepo thingRepo;

	@Autowired
	private TwinReactorReceiverFromThingRepo receiverRepo;

	@Autowired
	private ApplicationContext context;

	@Override
	public List<Message<? extends Telemetry>> react(Message<? extends Telemetry> imessage) {
		var thingId = LasRosasHeaders.thingid(imessage);
		var thing = thingRepo.findById(thingId).orElseThrow();
		var receivers = receiverRepo.findByThing(thing);

		// Find reactors to be triggered
		var result = new ArrayList<Message<? extends Telemetry>>();
		for(var receiver: receivers) {
			receiver.getType().getSchema();
			var receiverType = receiver.getType();
			var schema = LasRosasHeaders.schema(imessage);

			if(!schema.equals(receiverType.getSchema()))
				continue;

			var beanName = receiverType.getReactorType().getBean();

			var twin= receiver.getTwin();
			var reactor = context.getBean(beanName, TwinReactor.class);

			var resultPayloads= reactor.react(twin, receiver, imessage);
			for(var resultPayload: resultPayloads) {
				result.add(
						MessageBuilder.withPayload(resultPayload)
						.copyHeaders(imessage.getHeaders())
						.setHeader(LasRosasHeaders.TWIN_ID, twin.getTechid())
						.setHeader(LasRosasHeaders.TWIN_NATURAL_ID, twin.getName())
						.build());
			}
		}
		
		return result;
	}
}
