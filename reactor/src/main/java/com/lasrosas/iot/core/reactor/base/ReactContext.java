package com.lasrosas.iot.core.reactor.base;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

import com.lasrosas.iot.core.shared.telemetry.Order;
import com.lasrosas.iot.core.shared.telemetry.StateMessage;
import com.lasrosas.iot.core.shared.telemetry.Telemetry;

public class ReactContext {

	private static final ThreadLocal<Deque<ReactContext>> threadScopedContext = new ThreadLocal<Deque<ReactContext>>() {

		@Override
		protected Deque<ReactContext> initialValue() {
			return new ArrayDeque<ReactContext>();
		}
	};

	private final List<Telemetry> telemetries = new ArrayList<>();
	private final List<StateMessage> stateMessages = new ArrayList<>();
	private final List<Order> orders = new ArrayList<>();

	public static void push() {
		threadScopedContext.get().push(new ReactContext());
	}

	public static ReactContext peek() {
		return threadScopedContext.get().peek();
	}

	public static ReactContext pop() {
		return threadScopedContext.get().pop();
	}

	public static void addTelemetry(Telemetry telemery) {
		peek().telemetries.add(telemery);
	}

	public static void addStateMessage(StateMessage stateMessage) {
		peek().stateMessages.add(stateMessage);
	}

	public static void addOrder(Order order) {
		peek().orders.add(order);
	}

	public List<Telemetry> getTelemetries() {
		return telemetries;
	}

	public List<StateMessage> getStateMessages() {
		return stateMessages;
	}

	public List<Order> getOrders() {
		return orders;
	}
	
	public String encodeState() {
		return telemetries.size() + " telemetries, " + orders.size();
	}

	public static void clearAndPop() {
		threadScopedContext.get().clear();
		push();
	}
}
