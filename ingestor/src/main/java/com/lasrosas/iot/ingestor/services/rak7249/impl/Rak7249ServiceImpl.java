package com.lasrosas.iot.ingestor.services.rak7249.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.google.gson.Gson;
import com.lasrosas.iot.ingestor.LoraIngestor;
import com.lasrosas.iot.ingestor.services.lora.api.DeviceNameParser;
import com.lasrosas.iot.ingestor.services.lora.api.LoraMessageAck;
import com.lasrosas.iot.ingestor.services.lora.api.LoraMessageJoin;
import com.lasrosas.iot.ingestor.services.lora.api.LoraMessageUplink;
import com.lasrosas.iot.ingestor.services.lora.impl.DefaultDeviceNameParser;
import com.lasrosas.iot.ingestor.services.rak7249.api.Rak7249Message;
import com.lasrosas.iot.ingestor.services.rak7249.api.Rak7249MessageAck;
import com.lasrosas.iot.ingestor.services.rak7249.api.Rak7249MessageJoin;
import com.lasrosas.iot.ingestor.services.rak7249.api.Rak7249MessageRx;
import com.lasrosas.iot.ingestor.services.rak7249.api.Rak7249Service;
import com.lasrosas.iot.shared.utils.TimeUtils;

public class Rak7249ServiceImpl implements Rak7249Service {
	public static Logger log = LoggerFactory.getLogger(LoraIngestor.class);
	public static Logger payloadLog = LoggerFactory.getLogger("payload-rak7249");

	@Autowired
	private Gson gson;

	private DeviceNameParser deviceNameParser = new DefaultDeviceNameParser();

	public Rak7249Message fromJson(String topic, String json) {

		if(topic.endsWith("/join")) 
			gson.fromJson(json, Rak7249MessageJoin.class);
		
		if(topic.endsWith("/rx")) 
			gson.fromJson(json, Rak7249MessageRx.class);
		
		if(topic.endsWith("/ack")) 
			gson.fromJson(json, Rak7249MessageAck.class);

		throw new RuntimeException("Unkknown topic " + topic + ". Fix the code here.");
	}

	public LoraMessageUplink convertRxToLoraMessage(Rak7249MessageRx rxMessage) {
		logPayload(rxMessage);

		String deveui = rxMessage.getDevEUI();

		// Map to LoraMessage independent of the Lora server used
		var loraMessage = new LoraMessageUplink();
		loraMessage.setTime(TimeUtils.time(rxMessage.getTimestamp()));
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

		return loraMessage;
	}

	private void logPayload(Rak7249Message message) {
		if( payloadLog.isInfoEnabled() ) {
			try {
				var json = gson.toJson(message);
				payloadLog.info(json);
			} catch(Exception e) {
				payloadLog.error("Cannot log message", e);	// Should never be reached...
			}
		}
	}

	public LoraMessageJoin convertJoinToLoraMessage(Rak7249MessageJoin joinMessage) {
		logPayload(joinMessage);

		// Map to LoraMessage independent of the Lora server used
		var loraMessage = new LoraMessageJoin();

		loraMessage.setDeveui(joinMessage.getDevEUI());
		loraMessage.setGatewayId(joinMessage.getApplicationName());

		var deviceInfo = deviceNameParser.parse(joinMessage.getDeviceName());

		if(deviceInfo !=null) {
			loraMessage.setManufacturer(deviceInfo.getManufacturer());
			loraMessage.setManufacturer(deviceInfo.getModel());
		}

		return loraMessage;
	}

	public LoraMessageAck convertAckToLoraMessage(Rak7249MessageAck ackMessage) {
		var loraMessage = new LoraMessageAck();

		loraMessage.setDeveui(ackMessage.getDevEUI());
		loraMessage.setGatewayId(ackMessage.getApplicationName());

		return loraMessage;
	}

	public DeviceNameParser getDeviceNameParser() {
		return deviceNameParser;
	}

	public void setDeviceNameParser(DeviceNameParser deviceNameParser) {
		this.deviceNameParser = deviceNameParser;
	}
}
