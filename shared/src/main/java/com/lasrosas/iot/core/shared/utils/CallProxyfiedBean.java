package com.lasrosas.iot.core.shared.utils;

import java.util.function.Consumer;

public class CallProxyfiedBean {
	public <T> void call(Consumer<T> c, T t) {
		c.accept(t);
	}
}
