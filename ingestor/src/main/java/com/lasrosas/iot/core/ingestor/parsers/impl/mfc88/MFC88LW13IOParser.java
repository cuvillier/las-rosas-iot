package com.lasrosas.iot.core.ingestor.parsers.impl.mfc88;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;

import com.google.gson.Gson;
import com.lasrosas.iot.core.database.MessageUtils;
import com.lasrosas.iot.core.ingestor.parsers.api.ThingDataMessage;
import com.lasrosas.iot.core.ingestor.parsers.api.ThingEncodedMessage;
import com.lasrosas.iot.core.ingestor.parsers.impl.PayloadParser;
import com.lasrosas.iot.core.ingestor.parsers.impl.mfc88.MFC88LW13IOFrame.UplinkIO;
import com.lasrosas.iot.core.ingestor.parsers.impl.mfc88.MFC88LW13IOFrame.UplinkTimeSyncRequest;
import com.lasrosas.iot.core.ingestor.parsers.impl.mfc88.MFC88LW13IOFrame.UplinkTimeSyncRequest.UplinkTimeSyncRequestOption;
import com.lasrosas.iot.core.shared.telemetry.ConnectionStage;
import com.lasrosas.iot.core.shared.telemetry.Order;
import com.lasrosas.iot.core.shared.telemetry.Switched;
import com.lasrosas.iot.core.shared.telemetry.Telemetry;

public class MFC88LW13IOParser implements PayloadParser {
		public static final String MANUFACTURER = "MFC88";
		public static final String MODEL = "LW13IO";

		@Autowired
		public Gson gson;

		private MFC88LW13IOFrameDecoder decoder = new MFC88LW13IOFrameDecoder();

		@Override
		public Message<ThingDataMessage> decodeUplink(Message<ThingEncodedMessage> imessage) {
			return decoder.decodeUplink(imessage);
		}

		@Override
		public byte[] encodeOrder(Order order) {
			return decoder.encodeOrder(order);
		}

		@Override
		public List<Message<? extends Telemetry>> telemetries(Message<ThingDataMessage> imessage) {
			var result = new ArrayList<Message<? extends Telemetry>>();

			var payload = imessage.getPayload();
			if( payload instanceof UplinkIO) {

				var uplinkIO = (UplinkIO)payload;
				var switched = uplinkIO.getOutputs() != 0? Switched.on(): Switched.off();
				result.add(MessageUtils.buildMessage(imessage, switched).build());

			} else if( payload instanceof UplinkTimeSyncRequest) {

				var timeSyncRequest = (UplinkTimeSyncRequest)payload;
				if(timeSyncRequest.getOption() == UplinkTimeSyncRequestOption.AFTER_BOOT) {
					result.add(MessageUtils.buildMessage(imessage, ConnectionStage.joined()).build());
				}
			}

			return result;
		}

		@Override
		public boolean notifyJoin() {
			return true;
		}

		@Override
		public String getManufacturer() {
			return MANUFACTURER;
		}

		@Override
		public String getModel() {
			return MODEL;
		}
}
