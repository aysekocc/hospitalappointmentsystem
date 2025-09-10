package com.aysekoc.hospitalappointmantsystem.exception;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

@RestControllerAdvice
public class GeneralExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneralException(Exception ex) {
        ErrorResponse res = new ErrorResponse(LocalDateTime.now(),
                ex.getMessage(), ex.getStackTrace().toString(), 500);
        return new ResponseEntity<>(res, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<ErrorResponse> handleNoSuchElement(NoSuchElementException ex) {
        ErrorResponse res = new ErrorResponse(LocalDateTime.now(), "Data not Found ",
                ex.getStackTrace().toString(), 404);
        return new ResponseEntity<>(res, HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler(RuntimeException.class)
    public ErrorResponse handleRuntimeException(RuntimeException ex){
        ErrorResponse res = new ErrorResponse(LocalDateTime.now(),
                "not found data",
                ex.getStackTrace().toString(),
                400);
        return res;
    }
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();

        ex.getBindingResult().getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage()));

        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgument(IllegalArgumentException ex) {
        ErrorResponse res = new ErrorResponse(LocalDateTime.now(),
                "Bad Request ", ex.getStackTrace().toString(), null);
        return new ResponseEntity<>(res, HttpStatus.BAD_REQUEST);
    }


}