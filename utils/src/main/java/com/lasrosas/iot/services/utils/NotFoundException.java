package com.lasrosas.iot.services.utils;

public class NotFoundException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public NotFoundException(String whatIsNotFound) {
		super(whatIsNotFound + " not found");		
	}

}
