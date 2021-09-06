package com.lasrosas.iot.core.reactor.base;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
	public static final Logger log = LoggerFactory.getLogger(ReactorServiceImpl.class);

	@Autowired
	private ThingRepo thingRepo;

	@Autowired
	private TwinReactorReceiverFromThingRepo receiverRepo;

	@Autowired
	private ApplicationContext context;

	@Override
	public List<Message<? extends Telemetry>> react(Message<? extends Telemetry> imessage) {
		log.debug("react");

		/*
		 * Is the message come from another twin, skeep the message.
		 * Will be fixed later when needed.
		 */
		if( LasRosasHeaders.twinId(imessage).isPresent()) return Collections.emptyList();

		Long thingId = LasRosasHeaders.thingid(imessage).get();
		var thing = thingRepo.findById(thingId).orElseThrow();
		var receivers = receiverRepo.findByThing(thing);

		// Find reactors to be triggered
		var result = new ArrayList<Message<? extends Telemetry>>();
		for(var receiver: receivers) {
			receiver.getType().getSchema();
			var receiverType = receiver.getType();
			var schema = LasRosasHeaders.schema(imessage).get();
			var receiverSchema = receiverType.getSchema();

			if(receiverSchema.isPresent() && !schema.equals(receiverSchema.orElseThrow())) {
				log.debug("Receiver with different schema: " + receiverType.getSchema() + " != " + schema);
				continue;
			}

			var beanName = receiverType.getReactorType().getBean();

			var twin= receiver.getTwin();
			var reactor = context.getBean(beanName, TwinReactor.class);

			log.debug("Call reactor: " + beanName);

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

		log.debug("Reactor return " + result.size() + " messages.");

		return result;
	}
}
