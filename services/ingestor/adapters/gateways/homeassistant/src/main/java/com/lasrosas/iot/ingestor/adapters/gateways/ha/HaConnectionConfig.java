package com.lasrosas.iot.ingestor.adapters.gateways.ha;

import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.mqtt.core.DefaultMqttPahoClientFactory;
import org.springframework.integration.mqtt.core.MqttPahoClientFactory;

@Configuration
@Slf4j
public class HaConnectionConfig {

    // This logger is shared by all the Channels to log incoming / outgoing messages
    public static final Logger logMessage = LoggerFactory.getLogger("ingestor.adapters.gateways.ha.messages");

    @Value("${ingestor.adapters.gateways.ha.url:tcp://localhost:1883}")
    private String url;

    @Value("${ingestor.adapters.gateways.ha.username:lasrosasiot}")
    private String username;

    @Value("${ingestor.adapters.gateways.ha.password:lasrosasiot}")
    private String password;

    @Bean
    public MqttPahoClientFactory haClientFactory() {

        MqttConnectOptions options = new MqttConnectOptions();
        options.setMqttVersion(MqttConnectOptions.MQTT_VERSION_3_1_1);
        options.setServerURIs(new String[]{url});
        options.setAutomaticReconnect(true);

        DefaultMqttPahoClientFactory factory = new DefaultMqttPahoClientFactory();
        factory.setConnectionOptions(options);

        return factory;
    }
}
