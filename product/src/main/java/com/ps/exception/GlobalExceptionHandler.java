package com.ps.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * Global Exception handling
 */
@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(value = ProductNotFoundException.class)
    public ResponseEntity<Object> handleException(ProductNotFoundException exception) {
        return new ResponseEntity<>("Product Not Found", HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = PriceLimitExceeded.class)
    public ResponseEntity<Object> handleException(PriceLimitExceeded exception) {
        return new ResponseEntity<>("PriceLimitExceeded", HttpStatus.NOT_ACCEPTABLE);
    }
}
