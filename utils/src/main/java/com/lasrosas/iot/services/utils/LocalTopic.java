package com.lasrosas.iot.services.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class LocalTopic<T> {
	private List<Consumer<T>> consumers = new ArrayList<>();

	public synchronized void subcribe(Consumer<T> consumer) {
		consumers.add(consumer);
	}

	public void publish(T arg) {
		List<Consumer<T>> consumersCopy;

		synchronized(this) {
			consumersCopy = new ArrayList<Consumer<T>>(consumers);
		}

		for(var consumer : consumersCopy) {
			consumer.accept(arg);
		}
	}
}
