package com.lasrosas.iot.ingestor.services.lora.impl;

import org.springframework.beans.factory.annotation.Autowired;

import com.lasrosas.iot.database.entities.thg.ThingGateway;
import com.lasrosas.iot.database.entities.thg.ThingLora;
import com.lasrosas.iot.database.repo.GatewayRepo;
import com.lasrosas.iot.database.repo.ThingTypeRepo;
import com.lasrosas.iot.ingestor.services.lora.api.LoraMessageJoin;
import com.lasrosas.iot.ingestor.services.lora.api.LoraMessageUpload;
import com.lasrosas.iot.ingestor.services.lora.api.LoraService;
import com.lasrosas.iot.ingestor.shared.ThingMessage;
import com.lasrosas.iot.shared.utils.NotFoundException;

public class LoraServiceImpl implements LoraService {

	@Autowired
	private ThingTypeRepo thingTypeRepo;

	@Autowired
	private GatewayRepo gatewayRepo;
	
	private boolean autocreate = true;

	@Override
	public ThingMessage handleUpload(LoraMessageUpload uploadMessage) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void handleJoin(LoraMessageJoin joinMessage) {
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
