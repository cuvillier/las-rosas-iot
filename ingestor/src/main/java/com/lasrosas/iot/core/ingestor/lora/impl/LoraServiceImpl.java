package com.lasrosas.iot.core.ingestor.lora.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;

import com.lasrosas.iot.core.database.entities.thg.ThingGateway;
import com.lasrosas.iot.core.database.entities.thg.ThingLora;
import com.lasrosas.iot.core.database.repo.GatewayRepo;
import com.lasrosas.iot.core.database.repo.ThingLoraRepo;
import com.lasrosas.iot.core.database.repo.ThingTypeRepo;
import com.lasrosas.iot.core.ingestor.lora.api.LoraMessageJoin;
import com.lasrosas.iot.core.ingestor.lora.api.LoraMessageUplink;
import com.lasrosas.iot.core.ingestor.lora.api.LoraMetricMessage;
import com.lasrosas.iot.core.ingestor.lora.api.LoraService;
import com.lasrosas.iot.core.ingestor.sensors.api.ThingEncodedMessage;
import com.lasrosas.iot.core.shared.telemetry.ThingConnectionState;
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

	@Override
	public HandleUplinkResult splitUplink(Message<LoraMessageUplink> imessage) {
		var uploadMessage = imessage.getPayload();

		var loraMetric = new LoraMetricMessage();
		loraMetric.setCnt(uploadMessage.getCnt());
		loraMetric.setFrequency(uploadMessage.getFrequency());
		loraMetric.setPort(uploadMessage.getPort());
		loraMetric.setRssi(uploadMessage.getRssi());
		loraMetric.setSnr(uploadMessage.getSnr());

		var thingData = new ThingEncodedMessage();
		thingData.setEncodedData(uploadMessage.getData());
		thingData.setDataEncoding(uploadMessage.getDataEncoding());

		var thing = thingLoraRepo.getByDeveui(uploadMessage.getDeveui());
		if(thing == null )
			throw new NotFoundException("Thing deveui=" + uploadMessage.getDeveui());

		return new HandleUplinkResult(

			MessageBuilder
				.withPayload(thingData)
				.copyHeaders(imessage.getHeaders())
				.setHeader(LasRosasHeaders.THING_ID, thing.getTechid())
				.setHeader(LasRosasHeaders.THING_NATURAL_ID, "LOR" + thing.getDeveui())
				.build(),

			MessageBuilder
				.withPayload(loraMetric)
				.copyHeaders(imessage.getHeaders())
				.setHeader(LasRosasHeaders.THING_ID, thing.getTechid())
				.setHeader(LasRosasHeaders.THING_NATURAL_ID, "LOR" + thing.getDeveui())
				.build()
		);
	}

	@Override
	public ThingConnectionState splitJoin(Message<LoraMessageJoin> imessage) {

		var joinMessage = imessage.getPayload();

		var thing = thingLoraRepo.getByDeveui(joinMessage.getDeveui());
		if(thing == null ) {

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
	
			ThingGateway gateway = gatewayRepo.findByNaturalId(joinMessage.getGatewayId());
			if (gateway == null)
				throw new NotFoundException("Gateway " + joinMessage.getGatewayId());
	
			var thingLora = new ThingLora(gateway, tty, joinMessage.getDeveui());
	
			thingLoraRepo.save(thingLora);
		}

		return new ThingConnectionState(true, ThingConnectionState.CAUSE_NTW_CONNECT);
	}
}
