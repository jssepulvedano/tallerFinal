package com.bancolombia.tallerFinal.controllers;

import com.bancolombia.tallerFinal.exceptions.TransactionNotApprovedException;
import com.bancolombia.tallerFinal.exceptions.UserNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import reactor.core.publisher.Mono;

@RestControllerAdvice
public class GlobalHandleError {
    @ExceptionHandler({UserNotFoundException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Mono<ResponseEntity<String>> handleNoFoundException(UserNotFoundException exception){
        String message = exception.getMessage();
        return Mono.just(ResponseEntity.status(HttpStatus.NOT_FOUND).body(message));
    }
    @ExceptionHandler(TransactionNotApprovedException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Mono<ResponseEntity<String>> handleValidationExceptions(Exception  ex) {
        return Mono.just(ResponseEntity.status((HttpStatus.INTERNAL_SERVER_ERROR)).body(ex.getMessage()));
    }
}
