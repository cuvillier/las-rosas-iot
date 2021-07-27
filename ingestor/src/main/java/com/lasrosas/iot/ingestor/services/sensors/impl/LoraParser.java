package com.lasrosas.iot.ingestor.services.sensors.impl;

import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.lasrosas.iot.database.repo.ThingLoraRepo;
import com.lasrosas.iot.ingestor.services.lora.api.LoraMessage;
import com.lasrosas.iot.shared.utils.NotFoundException;

/*
 * Parse LoraMessages coming from the Gateways.
 * Decode the message payload and normalize it.
 */
public class LoraParser {

	@Autowired
	private ThingLoraRepo thgLoraRepo;
	
	@Autowired
	private PayloadParsers payloadParsers;

	public List<Object> parse(LoraMessage loraMessage) {

		var result = new ArrayList<Object>();

		var thing = thgLoraRepo.getByDeveui(loraMessage.getDeveui());
		if (thing == null)
			throw new NotFoundException("thing deveui=" + loraMessage.getDeveui());

		var thingType = thing.getType();

		var payloadParser = payloadParsers.getParser(thingType.getManufacturer(), thingType.getModel());
		if (payloadParser == null)
			throw new NotFoundException(
					"Sensor type manufacturer=" + thingType.getManufacturer() + ", model=" + thingType.getModel());

		if (thing.isLogLoraMessages())
			result.add(loraMessage);

		// Convert base64, hex to byte[]
		var data = decodeData(loraMessage);

		var decodedMessages = payloadParser.decodeUplink(data);

		for (var decodedMessage : decodedMessages) {

			if (thing.isLogDecodedMessages())
				result.add(decodedMessage);

			var normalizedMessages = payloadParser.normalize(decodedMessage);

			for (var normalizedMessage : normalizedMessages) {
				result.add(normalizedMessage);
			}
		}

		return result;
	}

	private byte[] decodeData(LoraMessage loraMessage) {
		var encoding = loraMessage.getDataEncoding();
		if (!encoding.equalsIgnoreCase("base64"))
			throw new RuntimeException("Unknown data encoding encoding=" + encoding);
	
		var encodedData = loraMessage.getData();
		var decodedData = Base64.getDecoder().decode(encodedData);
	
		return decodedData;
	}
}
