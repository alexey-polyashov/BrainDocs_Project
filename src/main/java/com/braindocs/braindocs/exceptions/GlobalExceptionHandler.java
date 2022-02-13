package com.braindocs.braindocs.exceptions;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.validation.ConstraintViolationException;
import java.util.List;
import java.util.stream.Collectors;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    @ExceptionHandler
    public ResponseEntity<?> notFoundException(ResourceNotFoundException e){
        log.error("Resource not found, {}", e.getMessage());
        return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    public ResponseEntity<?> catchInvalidInputDataException(MethodArgumentNotValidException e){
        List<Violation> violations = e.getBindingResult().getFieldErrors().stream()
                .map(error -> new Violation(error.getField(), error.getDefaultMessage()))
                .collect(Collectors.toList());
        return new ResponseEntity<>(violations, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public ResponseEntity<?> catchInvalidInputDataException(ConstraintViolationException e){
        List<Violation> violations = e.getConstraintViolations().stream()
                .map(error -> new Violation(error.getPropertyPath().toString(), error.getMessage()))
                .collect(Collectors.toList());
        return new ResponseEntity<>(violations, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public ResponseEntity<?> catchInvalidInputDataException(NotValidFields e){
        return new ResponseEntity<>(e.getViolations(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public ResponseEntity<?> catchOtherException(Exception e){
        log.error("Exception, {}", e.getMessage() + "\n"+ e.getStackTrace()[0]);
        return new ResponseEntity<>(e.getMessage() + "\n"+ e.getStackTrace()[0], HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
