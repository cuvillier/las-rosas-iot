package com.lasrosas.iot.ingestor.services.lora.impl;

import org.springframework.beans.factory.annotation.Autowired;

import com.lasrosas.iot.database.entities.thg.ThingGateway;
import com.lasrosas.iot.database.entities.thg.ThingLora;
import com.lasrosas.iot.database.repo.GatewayRepo;
import com.lasrosas.iot.database.repo.ThingLoraRepo;
import com.lasrosas.iot.database.repo.ThingTypeRepo;
import com.lasrosas.iot.ingestor.services.lora.api.LoraMessageJoin;
import com.lasrosas.iot.ingestor.services.lora.api.LoraMessageUplink;
import com.lasrosas.iot.ingestor.services.lora.api.LoraMetricMessage;
import com.lasrosas.iot.ingestor.services.lora.api.LoraService;
import com.lasrosas.iot.ingestor.services.sensors.api.ThingEncodedMessage;
import com.lasrosas.iot.shared.utils.NotFoundException;

public class LoraServiceImpl implements LoraService {

	@Autowired
	private ThingLoraRepo thingLoraRepo;

	@Autowired
	private ThingTypeRepo thingTypeRepo;

	@Autowired
	private GatewayRepo gatewayRepo;
	
	private boolean autocreate = true;

	@Override
	public HandleUplinkResult splitUplink(LoraMessageUplink uploadMessage) {
		var thing = thingLoraRepo.getByDeveui(uploadMessage.getDeveui());
		if(thing == null )
			throw new NotFoundException("Thing deveui=" + uploadMessage.getDeveui());

		var loraMetric = new LoraMetricMessage();
		loraMetric.setThingId(thing.getTechid());
		loraMetric.setCnt(uploadMessage.getCnt());
		loraMetric.setFrequency(uploadMessage.getFrequency());
		loraMetric.setPort(uploadMessage.getPort());
		loraMetric.setRssi(uploadMessage.getRssi());
		loraMetric.setSnr(uploadMessage.getSnr());
		loraMetric.setTime(uploadMessage.getTime());

		var thingData = new ThingEncodedMessage();
		thingData.setThingid(thing.getTechid());
		thingData.setTime(uploadMessage.getTime());
		thingData.setEncodedData(uploadMessage.getData());
		thingData.setDataEncoding(uploadMessage.getDataEncoding());

		return new HandleUplinkResult(thingData, loraMetric);
	}

	@Override
	public void splitJoin(LoraMessageJoin joinMessage) {

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

}
