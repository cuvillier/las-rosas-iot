package com.lasrosas.iot.ingestor.shared.exceptions;

public class InvalidJsonFormatException extends RuntimeException {
    public InvalidJsonFormatException(String message, Throwable cause) {
        super(message, cause);
    }
}
