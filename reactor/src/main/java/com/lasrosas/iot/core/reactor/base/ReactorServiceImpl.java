package com.lasrosas.iot.core.reactor.base;

import java.util.ArrayList;
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
	public List<Message<?>> react(Message<?> imessage) {
		var result = new ArrayList<Message<?>>();

		/*
		 * Is the message come from another twin, skeep the message.
		 * Will be fixed later when needed.
		 */
		if( LasRosasHeaders.twinId(imessage).isPresent()) return result;

		Long thingId = LasRosasHeaders.thingid(imessage).get();
		var thing = thingRepo.findById(thingId).orElseThrow();
		var receivers = receiverRepo.findByThing(thing);

		// Find reactors to be triggered
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

			try {
				ReactContext.push();

				reactor.react(receiver, imessage);
	
				var context = ReactContext.peek();

				for(var telemetry: context.getTelemetries()) {
					result.add(
							MessageBuilder.withPayload(telemetry)
							.copyHeaders(imessage.getHeaders())
							.setHeader(LasRosasHeaders.TWIN_ID, twin.getTechid())
							.setHeader(LasRosasHeaders.TWIN_NATURAL_ID, twin.getName())
							.build());
				}

				for(var stateMessage: context.getStateMessages()) {
					result.add(
							MessageBuilder.withPayload(stateMessage)
							.copyHeaders(imessage.getHeaders())
							.setHeader(LasRosasHeaders.TWIN_ID, twin.getTechid())
							.setHeader(LasRosasHeaders.TWIN_NATURAL_ID, twin.getName())
							.build());
				}

				for(var order: context.getOrders()) {
					var builder = MessageBuilder.withPayload(order)
						.copyHeaders(imessage.getHeaders())
						.setHeader(LasRosasHeaders.GATEWAY_NAURAL_ID, receiver.getThing().getGateway().getNaturalId())
						.setHeader(LasRosasHeaders.ORIGIN_TWIN, twin.getTechid())
						.setHeader(LasRosasHeaders.ORIGIN_TYPE, LasRosasHeaders.ORIGIN_TWIN);

					receiver.addOrderHeaders(builder);

					result.add(builder.build());
				}

			} finally {
				ReactContext.pop();
			}
		}

		log.debug("Reactor return " + result.size() + " messages.");

		return result;
	}
}
