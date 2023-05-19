package com.ryangrillo.warehousemgt.api.exception;

import org.hibernate.exception.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.ArrayList;
import java.util.List;

@ControllerAdvice
public class CustomExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ErrorResponse handleMethodArgumentNotValid(MethodArgumentNotValidException ex) {
        BindingResult result = ex.getBindingResult();
        List<String> errors = new ArrayList<>();
        for (FieldError error : result.getFieldErrors()) {
            errors.add(error.getDefaultMessage());
        }
        return new ErrorResponse("Validation Error", errors);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<String> handleRuntimeException(RuntimeException ex) {
        String errorMessage = "An unexpected error occurred";
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        if (ex instanceof DataFetchException) {
            errorMessage = ex.getMessage(); // Use the custom message from DataFetchException
            status = HttpStatus.NOT_FOUND;
        }
        if (ex instanceof DataInputException) {
            errorMessage = ex.getMessage(); // Use the custom message from DataFetchException
            status = HttpStatus.UNPROCESSABLE_ENTITY;
        }
        return ResponseEntity.status(status).body(errorMessage);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<String> handleConstraintViolationException(ConstraintViolationException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getLocalizedMessage());
    }


    @ExceptionHandler(EntityAlreadyExistsException.class)
    public ResponseEntity<String> handleWarehouseAlreadyExistsException(EntityAlreadyExistsException ex) {
        String errorMessage = ex.getMessage();
        HttpStatus status = HttpStatus.CONFLICT;
        return ResponseEntity.status(status).body(errorMessage);
    }

}

