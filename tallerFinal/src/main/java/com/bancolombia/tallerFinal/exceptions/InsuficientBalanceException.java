package com.bancolombia.tallerFinal.exceptions;

public class InsuficientBalanceException extends RuntimeException {
    public InsuficientBalanceException(String message) {
        super(message);
    }
}
