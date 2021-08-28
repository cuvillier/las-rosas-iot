package com.lasrosas.iot.core.shared.utils;

public class NotFoundException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public NotFoundException(String whatIsNotFound) {
		super(whatIsNotFound + " not found");		
	}

}
