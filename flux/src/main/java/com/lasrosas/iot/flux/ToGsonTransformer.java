package com.lasrosas.iot.flux;

import org.springframework.integration.transformer.AbstractTransformer;
import org.springframework.messaging.Message;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class ToGsonTransformer extends AbstractTransformer {
	private Gson gson = new GsonBuilder().setPrettyPrinting().create();

	@Override
	protected Object doTransform(Message<?> message) {
		return gson.toJson(message.getPayload());
	}
}
