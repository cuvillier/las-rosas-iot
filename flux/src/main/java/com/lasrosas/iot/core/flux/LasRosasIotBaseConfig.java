package com.lasrosas.iot.core.flux;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.integration.channel.PublishSubscribeChannel;
import org.springframework.integration.channel.QueueChannel;
import org.springframework.integration.dsl.MessageChannels;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.validation.annotation.Validated;

import com.google.gson.Gson;
import com.lasrosas.iot.core.ingestor.gateway.api.GatewayService;
import com.lasrosas.iot.core.ingestor.gateway.impl.GatewayServiceImpl;
import com.lasrosas.iot.core.ingestor.gateway.impl.rak7249.api.Rak7249Driver;
import com.lasrosas.iot.core.ingestor.gateway.impl.rak7249.impl.Rak7249DriverImpl;
import com.lasrosas.iot.core.ingestor.lora.api.LoraService;
import com.lasrosas.iot.core.ingestor.lora.impl.LoraServiceImpl;
import com.lasrosas.iot.core.ingestor.statemgt.api.ConnectionStateService;
import com.lasrosas.iot.core.ingestor.timeSerieWriter.api.InfluxDBConfig;
import com.lasrosas.iot.core.ingestor.timeSerieWriter.api.WriteInfluxDB;
import com.lasrosas.iot.core.ingestor.timeSerieWriter.api.WriteSQL;
import com.lasrosas.iot.core.ingestor.timeSerieWriter.impl.WriteInfluxDBImpl;
import com.lasrosas.iot.core.ingestor.timeSerieWriter.impl.WriteSQLImpl;
import com.lasrosas.iot.core.reactor.api.ReactorService;
import com.lasrosas.iot.core.reactor.base.ReactorServiceImpl;

@ConfigurationProperties
@Validated
public class LasRosasIotBaseConfig {
	public static final Logger log = LoggerFactory.getLogger(LasRosasIotBaseConfig.class);

	public static final String rak7249UplinkChannelName = "rak7249UplinkChannel";
	public static final String rak7249UplinkTxChannelName = "rak7249UplinkTxChannel";
	public static final String rak7249ChannelName = "rak7249Channel";
	public static final String loraChannelName = "loraChannel";
	public static final String errorChannelName = "errorChannel";
	public static final String mixedLoraChannelName = "mixedLoraChannel";
	public static final String loraMetricChannelName = "loraMetricChannel";
	public static final String thingEncodedDataChannelName = "thingEncodedDataChannel";
	public static final String thingDataChannelName = "thingDataChannel";
	public static final String thingBatteryChannelName = "thingBatteryChannel";
	public static final String telemetryChannelName = "telemetryChannel";
	public static final String alarmChannelName = "alarmChannel";
	public static final String publishMqttChannelName = "publishMqttChannel";
	public static final String twinOutputChannelName = "twinOutputChannel";
	public static final String orderChannelName = "orderChannel";
	public static final String downlinkChannelName = "downlinkChannel";

	@Autowired
	private Gson gson;

	@Bean
	public LasRosasFluxDelegate lasRosasFluxDelegate() {
		return new LasRosasFluxDelegate();
	}

	@Bean
	public MessageChannel errorChannel() {
		var queue = new QueueChannel(500);

		queue.addInterceptor(new ChannelInterceptor() {
			@Override
			public Message<?> preSend(Message<?> message, MessageChannel channel) {
				log.error("");
				log.error("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
				log.error(message.getPayload().getClass().getName());
				log.error(gson.toJson(message.getPayload()));
				return message;
			}
		});

		return queue;
	}

	@Bean
	public MessageChannel rak7249UplinkChannel() {
		return MessageChannels.direct().get();
	}

	@Bean
	public MessageChannel rak7249UplinkTxChannel() {
		return MessageChannels.publishSubscribe().get();
	}

	@Bean
	public PublishSubscribeChannel loraChannel() {
		return MessageChannels.publishSubscribe().get();
	}

	@Bean
	public MessageChannel loraJoinChannel() {
		return MessageChannels.direct().get();
	}

	@Bean
	public MessageChannel loraMetricChannel() {
		return MessageChannels.publishSubscribe().get();
	}

	@Bean
	public MessageChannel orderChannel() {
		return MessageChannels.direct().get();
	}

	@Bean
	public MessageChannel downlinkChannel() {
		return MessageChannels.direct().get();
	}

	@Bean
	public MessageChannel gatewayTechoFincaChannel() {
		return MessageChannels.direct().get();
	}

	@Bean
	public MessageChannel twinOutputChannel() {
		return MessageChannels.direct().get();
	}

	@Bean
	public MessageChannel sensorDataChannel() {
		return MessageChannels.publishSubscribe().get();
	}

	@Bean
	public MessageChannel mixedLoraChannel() {
		return MessageChannels.direct().get();
	}

	@Bean
	public MessageChannel thingEncodedDataChannel() {
		return MessageChannels.direct().get();
	}

	@Bean
	public MessageChannel thingDataChannel() {
		return MessageChannels.publishSubscribe().get();
	}

	@Bean
	public MessageChannel telemetryChannel() {
		return MessageChannels.publishSubscribe().get();
	}

	@Bean
	public MessageChannel connectionChannel() {
		return MessageChannels.publishSubscribe().get();
	}

	@Bean
	public MessageChannel publishMqttChannel() {
		return MessageChannels.direct().get();
	}

	@Bean
	public Rak7249Driver rak7249Driver() {
		return new Rak7249DriverImpl();
	}

	@Bean
	public LoraService loraService() {
		return new LoraServiceImpl();
	}

	@Bean
	public GatewayService gatewayService(Rak7249Driver rak7249) {
		return new GatewayServiceImpl(rak7249);
	}

	@Bean
	public ReactorService ReactorService() {
		return new ReactorServiceImpl();
	}

	@Bean
	@ConfigurationProperties(prefix = "influxdb")
	public InfluxDBConfig influxDBConfig() {
		return new InfluxDBConfig();
	}

	@Bean
	public WriteInfluxDB WriteInfluxDB(InfluxDBConfig influxDBConfig) {
		return new WriteInfluxDBImpl(influxDBConfig);
	}

	@Bean
	public WriteSQL WriteSQL() {
		return new WriteSQLImpl();
	}
}
