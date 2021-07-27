package com.lasrosas.iot.ingestor.services.rak7249.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;

import com.google.gson.Gson;
import com.lasrosas.iot.database.repo.ThingLoraRepo;
import com.lasrosas.iot.ingestor.LoraIngestor;
import com.lasrosas.iot.ingestor.services.lora.api.DeviceNameParser;
import com.lasrosas.iot.ingestor.services.lora.api.LoraMessage;
import com.lasrosas.iot.ingestor.services.lora.api.LoraMessageAck;
import com.lasrosas.iot.ingestor.services.lora.api.LoraMessageJoin;
import com.lasrosas.iot.ingestor.services.lora.api.LoraMessageUpload;
import com.lasrosas.iot.ingestor.services.lora.impl.DefaultDeviceNameParser;
import com.lasrosas.iot.ingestor.services.rak7249.api.Rak7249Message;
import com.lasrosas.iot.ingestor.services.rak7249.api.Rak7249MessageAck;
import com.lasrosas.iot.ingestor.services.rak7249.api.Rak7249MessageJoin;
import com.lasrosas.iot.ingestor.services.rak7249.api.Rak7249MessageRx;
import com.lasrosas.iot.ingestor.services.rak7249.api.Rak7249MessagesService;
import com.lasrosas.iot.shared.utils.NotFoundException;

public class Rak7249MessagesServiceImpl implements Rak7249MessagesService {
	public static Logger log = LoggerFactory.getLogger(LoraIngestor.class);
	public static Logger payloadLog = LoggerFactory.getLogger("payload-rak7249");

	@Autowired
	private ThingLoraRepo thgLorRepo;

	@Autowired
	private Gson gson;

	private DeviceNameParser deviceNameParser = new DefaultDeviceNameParser();

	public LoraMessageUpload handleRx(Message<Rak7249MessageRx> imessage) {
		logPayload(imessage);

		var rxMessage = imessage.getPayload();
		String deveui = rxMessage.getDevEUI();

		var thgLora = thgLorRepo.getByDeveui(deveui);
		if (thgLora == null)
			throw new NotFoundException("Thing Lora deveui=" + deveui);

		// Map to LoraMessage independent of the Lora server used
		var loraMessage = new LoraMessageUpload();
		loraMessage.setTimestamp(rxMessage.getTimestamp());
		loraMessage.setDeveui(deveui);
		loraMessage.setData(rxMessage.getData());
		loraMessage.setDataEncoding(rxMessage.getDataEncode());
		loraMessage.setCnt(rxMessage.getFCnt());
		loraMessage.setPort(rxMessage.getFPort());

		if( rxMessage.getRxInfo() != null) {
			loraMessage.setRssi(rxMessage.getRxInfo().getRssi());
			loraMessage.setSnr(rxMessage.getRxInfo().getLoRaSNR());
		}

		if( rxMessage.getTxInfo() != null) {
			loraMessage.setFrequency(rxMessage.getTxInfo().getFrequency());
		}

		copyHeaders(imessage, loraMessage);

		return loraMessage;
	}

	private void copyHeaders(Message<? extends Rak7249Message> imessage, LoraMessage loraMessage) {
		loraMessage.setTopic((String) imessage.getHeaders().get("mqtt_receivedTopic"));
		loraMessage.setDuplicate((Boolean) imessage.getHeaders().get("mqtt_duplicate"));
	}

	private void logPayload(Message<?> imessage) {
		if( payloadLog.isInfoEnabled() ) {
			try {
				var json = gson.toJson(imessage);
				payloadLog.info(json);
			} catch(Exception e) {
				payloadLog.error("Cannot log message", e);	// Should never be reached...
			}
		}
	}

	public LoraMessageJoin handleJoin(Message<Rak7249MessageJoin> imessage) {
		logPayload(imessage);

		// Map to LoraMessage independent of the Lora server used
		var joinMessage = imessage.getPayload();
		var loraMessage = new LoraMessageJoin();

		loraMessage.setDeveui(joinMessage.getDevEUI());
		loraMessage.setGatewayId(joinMessage.getApplicationName());

		var deviceInfo = deviceNameParser.parse(joinMessage.getDeviceName());

		if(deviceInfo !=null) {
			loraMessage.setManufacturer(deviceInfo.getManufacturer());
			loraMessage.setManufacturer(deviceInfo.getModel());
		}

		copyHeaders(imessage, loraMessage);

		return loraMessage;
	}

	public LoraMessageAck handleAck(Message<Rak7249MessageAck> imessage) {
		logPayload(imessage);
		var ackMessage = imessage.getPayload();
		var loraMessage = new LoraMessageAck();

		loraMessage.setDeveui(ackMessage.getDevEUI());
		loraMessage.setGatewayId(ackMessage.getApplicationName());

		copyHeaders(imessage, loraMessage);

		return loraMessage;
	}

	public DeviceNameParser getDeviceNameParser() {
		return deviceNameParser;
	}

	public void setDeviceNameParser(DeviceNameParser deviceNameParser) {
		this.deviceNameParser = deviceNameParser;
	}
}
