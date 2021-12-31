package com.lasrosas.iot.core.ingestor.gateway.impl.rak7249.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;

import com.google.gson.Gson;
import com.lasrosas.iot.core.ingestor.gateway.impl.rak7249.api.Rak7249Driver;
import com.lasrosas.iot.core.ingestor.gateway.impl.rak7249.api.Rak7249Message;
import com.lasrosas.iot.core.ingestor.gateway.impl.rak7249.api.Rak7249MessageAck;
import com.lasrosas.iot.core.ingestor.gateway.impl.rak7249.api.Rak7249MessageJoin;
import com.lasrosas.iot.core.ingestor.gateway.impl.rak7249.api.Rak7249MessageRx;
import com.lasrosas.iot.core.ingestor.lora.api.DeviceNameParser;
import com.lasrosas.iot.core.ingestor.lora.api.LoraMessageAck;
import com.lasrosas.iot.core.ingestor.lora.api.LoraMessageJoin;
import com.lasrosas.iot.core.ingestor.lora.api.LoraMessageUplink;
import com.lasrosas.iot.core.ingestor.lora.impl.DefaultDeviceNameParser;

public class Rak7249DriverImpl implements Rak7249Driver {
	public static final Logger log = LoggerFactory.getLogger(Rak7249DriverImpl.class);

	@Autowired
	private Gson gson;

	private DeviceNameParser deviceNameParser = new DefaultDeviceNameParser();

	public Rak7249DriverImpl() {
		super();
	}

	@Override
	public Rak7249Message fromJson(String topic, String json) {
		Rak7249Message message;
		if(topic.endsWith("/join")) 
			message = gson.fromJson(json, Rak7249MessageJoin.class);
		
		else if(topic.endsWith("/rx")) 
			message = gson.fromJson(json, Rak7249MessageRx.class);
		
		else if(topic.endsWith("/ack")) 
			message = gson.fromJson(json, Rak7249MessageAck.class);
		else
			throw new RuntimeException("Unkknown topic " + topic + ". Fix the code here.");

		return message;
	}

	@Override
	public Message<LoraMessageUplink> convertRxToLoraMessage(Message<Rak7249MessageRx> imessage) {
		var rxMessage = imessage.getPayload();

		String deveui = rxMessage.getDevEUI();

		// Map to LoraMessage independent of the Lora server used
		var loraMessage = new LoraMessageUplink();
		loraMessage.setDeveui(deveui);
		loraMessage.setData(rxMessage.getData());
		loraMessage.setDataEncoding(rxMessage.getData_encode());
		loraMessage.setCnt(rxMessage.getFCnt());
		loraMessage.setPort(rxMessage.getFPort());

		if( rxMessage.getRxInfo() != null) {
			loraMessage.setRssi(rxMessage.getRxInfo().get(0).getRssi());
			loraMessage.setSnr(rxMessage.getRxInfo().get(0).getLoRaSNR());
		}

		if( rxMessage.getTxInfo() != null) {
			loraMessage.setFrequency(rxMessage.getTxInfo().getFrequency());
		}

		return MessageBuilder.createMessage(loraMessage, imessage.getHeaders());
	}

	@Override
	public Message<LoraMessageJoin> convertJoinToLoraMessage(Message<Rak7249MessageJoin> imessage) {
		var joinMessage = imessage.getPayload();

		// Map to LoraMessage independent of the Lora server used
		var loraMessage = new LoraMessageJoin();

		loraMessage.setDeveui(joinMessage.getDevEUI());
		loraMessage.setGatewayId(joinMessage.getApplicationName());

		var deviceInfo = deviceNameParser.parse(joinMessage.getDeviceName());

		if(deviceInfo !=null) {
			loraMessage.setManufacturer(deviceInfo.getManufacturer());
			loraMessage.setManufacturer(deviceInfo.getModel());
		}

		return MessageBuilder.createMessage(loraMessage, imessage.getHeaders());
	}

	@Override
	public Message<LoraMessageAck> convertAckToLoraMessage(Message<Rak7249MessageAck> imessage) {
		var ackMessage = imessage.getPayload();

		var loraMessage = new LoraMessageAck();

		loraMessage.setDeveui(ackMessage.getDevEUI());
		loraMessage.setGatewayId(ackMessage.getApplicationName());

		return MessageBuilder.createMessage(loraMessage, imessage.getHeaders());
	}

	public DeviceNameParser getDeviceNameParser() {
		return deviceNameParser;
	}

	public void setDeviceNameParser(DeviceNameParser deviceNameParser) {
		this.deviceNameParser = deviceNameParser;
	}

	@Override
	public String encodeDownlink(byte[] data) {
		var hexData = javax.xml.bind.DatatypeConverter.printHexBinary(data);
		return "{\"confirmed\": true, \"fPort\": 1, \"data\": \""+ hexData +"\"}";
	}
}
