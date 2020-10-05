package com.lasrosas.iot.reactore.reactores;

public interface TwinReactor {

	void handle(String json, String twinId);

}
