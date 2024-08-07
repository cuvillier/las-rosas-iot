package com.lasrosas.iot.ingestor.adapters.presenters.controllers;

import com.lasrosas.iot.ingestor.shared.exceptions.NotFoundException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @GetMapping("/test/ping")
    public String ping() {
        return "pong";
    }

    @GetMapping("/test/exception/NotFoundException")
    public void  NotFoundException() {
        throw new NotFoundException("Something missing for testing");
    }

    @GetMapping("/test/exception/NullPointerException")
    public void NullPointerException() {
        throw new NullPointerException("Something null for testing");
    }
}
