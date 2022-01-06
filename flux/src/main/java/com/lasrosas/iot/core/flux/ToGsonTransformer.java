package com.lasrosas.iot.core.flux;

import org.springframework.integration.transformer.AbstractTransformer;
import org.springframework.messaging.Message;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class ToGsonTransformer extends AbstractTransformer {
	private Gson gson = new GsonBuilder().setPrettyPrinting().create();

	@Override
	protected Object doTransform(Message<?> message) {
		var json = gson.toJson(message.getPayload());
		return "{\n" 
			   + "  \"schema\": \"" + message.getPayload().getClass().getSimpleName() + "\",\n"
		       + "  \"payload\":" + json + "\n"
		       + "}";
				
	}
}
