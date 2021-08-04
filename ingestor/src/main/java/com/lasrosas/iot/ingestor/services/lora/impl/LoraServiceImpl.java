package com.lasrosas.iot.ingestor.services.lora.impl;

import org.springframework.beans.factory.annotation.Autowired;

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
		if(thing == null ) throw new NotFoundException("Thing deveui=" + uploadMessage.getDeveui());

		var loraMetric = new LoraMetricMessage();
		loraMetric.setThingid(thing.getTechid());
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
			if( !autocreate )
				throw new RuntimeException("Unknown Thing devEUI=" + deveui);

			if (splitedName.length != 3) {
				throw new RuntimeException(
						"Invalid Lora device name, should be manufacturer/model/deveui: " + deviceName);
			}

			String manufacturer = splitedName[0];
			String model = splitedName[1];
			var tty = thingTypeRepo.getByManufacturerAndModel(manufacturer, model);
			if (tty == null)
				throw new NotFoundException("Thing type for device name=" + deviceName);

			ThingGateway gateway = gatewayRepo.findByNaturalId(joinMessage.getApplicationName());
			if (gateway == null)
				throw new NotFoundException("Gateway " + joinMessage.getApplicationName());

			thingLora = new ThingLora(gateway, tty, deveui);

			thgLorRepo.save(thingLora);

		 */
	}

}
