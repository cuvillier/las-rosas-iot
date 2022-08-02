package com.lasrosas.iot.core.ingestor.parsers.api;

import org.springframework.integration.transformer.AbstractTransformer;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;

import com.lasrosas.iot.core.ingestor.gateway.api.GatewayService;
import com.lasrosas.iot.core.shared.telemetry.Order;
import com.lasrosas.iot.core.shared.utils.LasRosasHeaders;

public class DownlinkEncoderTransformer extends AbstractTransformer {

	private SensorService sensorService;
	private GatewayService gatewayService;

	public DownlinkEncoderTransformer(SensorService service, GatewayService gatewayService) {
		this.sensorService = service;
		this.gatewayService = gatewayService;
	}

	@SuppressWarnings("unchecked")
	@Override
	protected Object doTransform(Message<?> imessage) {

		if(imessage.getPayload() instanceof Order) {
			var gatewayNaturalId = LasRosasHeaders.gatewayNaturalId(imessage).get();

			// Sensor encoding
			byte [] data = sensorService.encodeOrder((Message<? extends Order>)imessage);

			var json = gatewayService.encodeDownlink(gatewayNaturalId, data);

			// Gateway message wrapping
			var result  = MessageBuilder.withPayload(json)
					.copyHeaders(imessage.getHeaders())
					.build();

			return result;
		}

		return null;
	}
}
