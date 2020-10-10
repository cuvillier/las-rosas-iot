package com.lasrosas.iot.shared.utils;

import java.util.function.Consumer;

public class CallProxyfiedBean {
	public <T> void call(Consumer<T> c, T t) {
		c.accept(t);
	}
}
