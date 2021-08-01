package com.lasrosas.iot.ingestor.services.rak7249.api;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public abstract class Rak7249Message {
	private final Map<String, Object> headers = new HashMap<String, Object>();

	public void addheader(String name, Object value) {
		headers.put(name, value);
	}

	@SuppressWarnings("unchecked")
	public <T> T getHeader(String name, Class<T> eclass) {
		return (T)headers.get(name);
	}

	public Iterator<Map.Entry<String, Object>> headers() {
		return headers.entrySet().iterator();
	}
}
