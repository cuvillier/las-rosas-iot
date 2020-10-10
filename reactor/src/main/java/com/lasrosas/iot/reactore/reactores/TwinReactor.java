package com.lasrosas.iot.reactore.reactores;

import java.util.List;

import com.google.gson.JsonArray;
import com.lasrosas.iot.database.entities.dtw.DigitalTwin;

public interface TwinReactor {
	List<TransmitterValue> react(DigitalTwin twin, List<ReceiverValue> values);

	List<ReceiverValue> mapReceiverValues(JsonArray values);
}
