package com.lasrosas.iot.ingestor.shared.exceptions;

public class NotFoundException extends RuntimeException {
    private String what;

    public NotFoundException(String what) {
        super(what + ": Not found");
    }
}
