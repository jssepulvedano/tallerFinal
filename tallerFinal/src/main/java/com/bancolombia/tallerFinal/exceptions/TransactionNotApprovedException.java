package com.bancolombia.tallerFinal.exceptions;

public class TransactionNotApprovedException extends RuntimeException {
    public TransactionNotApprovedException(String message) {
        super(message);
    }
}
