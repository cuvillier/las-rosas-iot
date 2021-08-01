package com.lasrosas.iot.ingestor;

public class SendMessageToTwin {
/*
	public static final Logger log = LoggerFactory.getLogger(SendMessageToTwin.class);

	@Autowired
	private Gson gson;

	private MqttSession mqtt;

	public SendMessageToTwin(MqttSession mqtt) {
		this.mqtt = mqtt;
	}

	public void send(List<TimeSeriePoint> points, long txid) {
		try {
			if(points.size() == 0) return;

			var thing = points.get(0).getTimeSerie().getThing();

			String topic = "thing/" + thing.getType().getManufacturer() + "-" +  thing.getType().getModel() + "/" + thing.getTechid() + "/measurements";

			var message = new Telemetry(System.nanoTime(), LocalDateTime.now());

			for(var point: points) {
				message.getPoints().add(
						new Telemetry.Point(
								point.getTechid(), 
								point.getTimeSerie().getType().getSchema(),
								point.getValue()));
			}

			var messageJson = gson.toJson(message);
			var messageMQTT = new MqttMessage(messageJson.getBytes());
			log.info("Publish topic=" + topic + " message=" + new String(messageMQTT.getPayload()));
			this.mqtt.publish(topic, messageMQTT);

		} catch (RuntimeException e) {
			throw e;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
*/
}
