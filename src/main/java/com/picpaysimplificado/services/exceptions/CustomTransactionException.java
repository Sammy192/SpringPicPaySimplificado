package com.picpaysimplificado.services.exceptions;

public class CustomTransactionException extends RuntimeException {

    public CustomTransactionException(String msg) {
        super(msg);
    }
}