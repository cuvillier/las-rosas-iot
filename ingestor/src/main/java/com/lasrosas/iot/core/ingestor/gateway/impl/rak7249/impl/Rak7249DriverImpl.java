package com.lasrosas.iot.core.ingestor.gateway.impl.rak7249.impl;

import java.time.LocalDateTime;
import java.util.Base64;

import org.junit.jupiter.api.Assertions;
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
import com.lasrosas.iot.core.ingestor.lora.api.LoraMessage;
import com.lasrosas.iot.core.ingestor.lora.api.LoraMessageAck;
import com.lasrosas.iot.core.ingestor.lora.api.LoraMessageJoin;
import com.lasrosas.iot.core.ingestor.lora.api.LoraMessageUplink;
import com.lasrosas.iot.core.ingestor.lora.impl.DefaultDeviceNameParser;
import com.lasrosas.iot.core.shared.utils.LasRosasHeaders;

public class Rak7249DriverImpl implements Rak7249Driver {
	public static final Logger log = LoggerFactory.getLogger(Rak7249DriverImpl.class);

	@Autowired
	private Gson gson;

	private DeviceNameParser deviceNameParser = new DefaultDeviceNameParser();

	public Rak7249DriverImpl() {
		super();
	}

	@SuppressWarnings("unchecked")
	@Override
	public Message< ? extends LoraMessage> transform(Message<? extends Rak7249Message> message) {
		log.info("doTransform procssing message=" + message);

		var payload = message.getPayload();

		Message< ? extends LoraMessage> loraMessage;

		if(payload instanceof Rak7249MessageJoin)
			loraMessage = convertJoinToLoraMessage((Message<Rak7249MessageJoin>)message);

		else if(payload instanceof Rak7249MessageAck)
			loraMessage = convertAckToLoraMessage((Message<Rak7249MessageAck>)message);

		else if(payload instanceof Rak7249MessageRx)
			loraMessage = convertRxToLoraMessage((Message<Rak7249MessageRx>)message);
		else
			throw new RuntimeException("Unknown message type: " + message.getPayload().getClass().getName());

		log.info("doTransform loraMessage = " + loraMessage);
		return loraMessage;
	}

	@Override
	public Rak7249Message fromJson(String topic, String json) {
		Rak7249Message message;

		if(topic.endsWith("/join")) {
			var join= gson.fromJson(json, Rak7249MessageJoin.class);
			message = join;

		} else if(topic.endsWith("/rx")) {
			var rx = gson.fromJson(json, Rak7249MessageRx.class);
			if(rx.getDevEUI() == null)
				log.info("null deveui");
			message = rx;

		} else if(topic.endsWith("/ack")) {
			var ack = gson.fromJson(json, Rak7249MessageAck.class);
			if(ack.getDevEUI() == null)
				log.info("null deveui");
			message = ack;

		} else {
			log.warn("Message ignored because this is an unknown topic " + topic);
			log.warn(json);

			return null;
		}
		
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

		if( rxMessage.getRxInfo() != null && rxMessage.getRxInfo().size() > 0) {
			loraMessage.setRssi(rxMessage.getRxInfo().get(0).getRssi());
			loraMessage.setSnr(rxMessage.getRxInfo().get(0).getLoRaSNR());
		}

		if( rxMessage.getTxInfo() != null) {
			loraMessage.setFrequency(rxMessage.getTxInfo().getFrequency());
		}

		return buildMessage(loraMessage, imessage);
	}

	/**
	 * Insure the message timeReceived is set or propagated.
	 * @param <T>
	 * @param payload
	 * @param imessage
	 * @return
	 */
	private <T> Message<T> buildMessage(T payload, Message<?> imessage) {
		var time = LasRosasHeaders.timeReceived(imessage);
		if( time == null ) time = LocalDateTime.now();
		return MessageBuilder.withPayload(payload)
				.copyHeaders(imessage.getHeaders())
				.setHeader(LasRosasHeaders.TIME_RECEIVED, time)
				.build();
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
			loraMessage.setModel(deviceInfo.getModel());
		}

		log.info("------------------------------------- JOIN " + joinMessage.getDevEUI());
		log.info("Class = " + joinMessage.getClass().getSimpleName());
		log.info(gson.toJson(joinMessage));

		return buildMessage(loraMessage, imessage);
	}

	@Override
	public Message<LoraMessageAck> convertAckToLoraMessage(Message<Rak7249MessageAck> imessage) {
		var ackMessage = imessage.getPayload();

		log.info("------------------------------------- ACK " + ackMessage.getDevEUI());
		log.info("Class = " + ackMessage.getClass().getSimpleName());
		log.info(gson.toJson(ackMessage));

		var loraMessage = new LoraMessageAck();

		loraMessage.setDeveui(ackMessage.getDevEUI());
		loraMessage.setGatewayId(ackMessage.getApplicationName());

		return buildMessage(loraMessage, imessage);
	}

	public DeviceNameParser getDeviceNameParser() {
		return deviceNameParser;
	}

	public void setDeviceNameParser(DeviceNameParser deviceNameParser) {
		this.deviceNameParser = deviceNameParser;
	}

	@Override
	public String encodeDownlink(byte[] data) {
		var encoded = Base64.getEncoder().encodeToString(data);
		return "{\"confirmed\": true, \"fPort\": 6, \"data\": \""+ encoded +"\"}";
	}

	@Override
	public String typeName() {
		return Rak7249Driver.TYPE_NAME;
	}
}
