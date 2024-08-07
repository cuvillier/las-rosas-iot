package com.lasrosas.iot.ingestor.adapters.presenters;

import com.lasrosas.iot.ingestor.shared.exceptions.NotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler({ NotFoundException.class})
    protected ResponseEntity<String> handleConflict(RuntimeException ex, HttpServletRequest httpRequest) {
        String body = ex.getMessage();
        log.info(body, ex);
        return new ResponseEntity<String>(body, new HttpHeaders(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(Exception.class)
    public final ResponseEntity<String> handleAllExceptions(Exception ex, HttpServletRequest httpRequest) {
        var body = httpRequest.getRequestURI() + " failed.";
        log.error(body, ex);
        return new ResponseEntity<String>(body, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
