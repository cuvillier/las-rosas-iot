package com.lasrosas.iot.shared.utils.diffuse;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Diffuse {
	public Map<Class<?>, List<DiffuseListener>> eventClassesIndex = new HashMap<>();

	public synchronized void subscribe(DiffuseMatcher matcher, DiffuseListener listener) {
		var listeners = eventClassesIndex.get(matcher.getEventDataClass());

		if(listeners == null) {
			listeners = new ArrayList<>();
			eventClassesIndex.put(matcher.getEventDataClass(), listeners);
		}

		if( listeners.indexOf(listener) == -1 ) {
			listeners.add(listener);
		}
	}

	public synchronized boolean unsubscribe(DiffuseListener listener, DiffuseMatcher matcher) {
		var listeners = eventClassesIndex.get(matcher.getEventDataClass());

		if(listeners != null)
			return listeners.remove(listener);

		return false;
	}

	public boolean publish(DiffuseEvent event) {
		List<DiffuseListener> listeners;
		synchronized(this) {
			var listenersToBeCopied = eventClassesIndex.get(event.getValue().getClass());
			if( listenersToBeCopied == null) return false;
			listeners = new ArrayList<>(listenersToBeCopied);
		}

		// Unsynchronized block
		for(var l: listeners) {
			l.diffuse(event);
		}

		return true;
	}
}
