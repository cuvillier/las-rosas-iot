package com.lasrosas.iot.core.ingestor.lora.impl;

import java.time.LocalDateTime;
import java.util.ArrayList;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;

import com.lasrosas.iot.core.database.entities.thg.Thing;
import com.lasrosas.iot.core.database.entities.thg.ThingGateway;
import com.lasrosas.iot.core.database.entities.thg.ThingLora;
import com.lasrosas.iot.core.database.repo.GatewayRepo;
import com.lasrosas.iot.core.database.repo.ThingLoraRepo;
import com.lasrosas.iot.core.database.repo.ThingTypeRepo;
import com.lasrosas.iot.core.ingestor.MessageUtils;
import com.lasrosas.iot.core.ingestor.lora.api.LoraMessage;
import com.lasrosas.iot.core.ingestor.lora.api.LoraMessageJoin;
import com.lasrosas.iot.core.ingestor.lora.api.LoraMessageUplink;
import com.lasrosas.iot.core.ingestor.lora.api.LoraMetricMessage;
import com.lasrosas.iot.core.ingestor.lora.api.LoraService;
import com.lasrosas.iot.core.ingestor.parsers.api.SensorService;
import com.lasrosas.iot.core.ingestor.parsers.api.ThingEncodedMessage;
import com.lasrosas.iot.core.ingestor.statemgt.api.ConnectionStateService;
import com.lasrosas.iot.core.shared.telemetry.ConnectionStage;
import com.lasrosas.iot.core.shared.telemetry.ConnectionState;
import com.lasrosas.iot.core.shared.utils.LasRosasHeaders;
import com.lasrosas.iot.core.shared.utils.NotFoundException;

public class LoraServiceImpl implements LoraService {

	@Autowired
	private ThingLoraRepo thingLoraRepo;

	@Autowired
	private ThingTypeRepo thingTypeRepo;

	@Autowired
	private GatewayRepo gatewayRepo;

	private boolean autocreate = true;

	@PersistenceContext
	private EntityManager em;

	@Autowired
	private ConnectionStateService ctxStateService;

	@Autowired
	private SensorService sensorService;

	@Override
	public ArrayList<Message<?>> splitMessage(Message<?> imessage) {
		var result = new ArrayList<Message<?>>();
		var payload = (LoraMessage)imessage.getPayload();
		ThingLora thing = null;

		if(payload instanceof LoraMessageUplink ) {
			thing = thingLoraRepo.getByDeveui(payload.getDeveui()).get();

			@SuppressWarnings("unchecked")
			var splitResult = splitUplink((Message<LoraMessageUplink>)imessage);

			result.add(splitResult.getLoraMetricMessage());
			result.add(splitResult.getThingEncodedMessage());

			var time = LasRosasHeaders.timeReceived(imessage);
			if( time == null ) time = LocalDateTime.now();

			if( ctxStateService.alive(time, thing) ) {
				result.add(MessageUtils.buildMessage(imessage, thing, ConnectionState.connected()).build());
			}

		} else if(payload instanceof LoraMessageJoin ) {
			var join = (LoraMessageJoin)payload;
			thing = autoCreateThing((Message<LoraMessageJoin>)imessage);

			var thingType = thing.getType();

			if( sensorService.isNotifyJoin(thingType.getManufacturer(), thingType.getModel())) {
				var stateIMessage = MessageUtils.buildMessage(imessage, thing, ConnectionStage.joining()).build();
				result.add(stateIMessage);
			}
		} else {
			if( thingLoraRepo.getByDeveui(payload.getDeveui()).isPresent() ) {
				thing = thingLoraRepo.getByDeveui(payload.getDeveui()).get();
			}
		}

		return result;
	}

	@Override
	public HandleUplinkResult splitUplink(Message<LoraMessageUplink> imessage) {
		var uploadMessage = imessage.getPayload();

		if( uploadMessage.getDeveui() == null)
			throw new RuntimeException("DevEUI is missing in uploadMessage");

		var loraMetric = new LoraMetricMessage();
		loraMetric.setCnt(uploadMessage.getCnt());
		loraMetric.setFrequency(uploadMessage.getFrequency());
		loraMetric.setPort(uploadMessage.getPort());
		loraMetric.setRssi(uploadMessage.getRssi());
		loraMetric.setSnr(uploadMessage.getSnr());

		var thingData = new ThingEncodedMessage();
		thingData.setEncodedData(uploadMessage.getData());
		thingData.setDataEncoding(uploadMessage.getDataEncoding());

		var thing = thingLoraRepo.getByDeveui(uploadMessage.getDeveui()).get();

		return new HandleUplinkResult(
			MessageUtils.buildMessage(imessage, thing, thingData).build(),
			MessageUtils.buildMessage(imessage, thing, loraMetric).build()
		);
	}

	public ThingLora autoCreateThing(Message<LoraMessageJoin> imessage) {
		var joinMessage = imessage.getPayload();

		var thing = thingLoraRepo.getByDeveui(joinMessage.getDeveui()).orElse(null);
		if(thing == null) {

			/*
			 * If the thing doesnot exists, try to create it.
			 */
			if( !autocreate )
				throw new RuntimeException("Unknown Thing devEUI=" + joinMessage.getDeveui());

			String manufacturer = joinMessage.getManufacturer();
			String model = joinMessage.getModel();
			if(manufacturer == null || model == null) 
				throw new RuntimeException("Cannot create thing, manufacturer=" + manufacturer + ", model=" + model);

			var tty = thingTypeRepo.getByManufacturerAndModel(manufacturer, model);
			if (tty == null)
				throw new NotFoundException("Thing type for device name=" + joinMessage.getDeveui());
	
			ThingGateway gateway = gatewayRepo.findByNaturalId(joinMessage.getGatewayId()).get();

			thing = new ThingLora(gateway, tty, joinMessage.getDeveui());

			thingLoraRepo.save(thing);
		}

		return thing;
	}
}
