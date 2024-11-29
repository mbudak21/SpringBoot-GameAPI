package com.dreamgames.backendengineeringcasestudy.util;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.server.ResponseStatusException;

import java.util.Map;
import java.util.Objects;

//@ControllerAdvice
//public class GlobalExceptionHandler {
//
//    @ExceptionHandler(ResponseStatusException.class)
//    public ResponseEntity<Object> handleResponseStatusException(ResponseStatusException ex) {
//        // Create a structured response (or just return the message if needed)
//        return ResponseEntity
//                .status(ex.getStatusCode())
//                .body(Map.of(
//                        "status", ex.getStatusCode(),
//                        "error", Objects.requireNonNull(ex.getReason()),
//                        "message", ex.
//                ));
//    }
//}
