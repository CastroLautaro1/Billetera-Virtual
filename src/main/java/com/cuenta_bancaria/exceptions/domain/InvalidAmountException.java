package com.cuenta_bancaria.exceptions.domain;

public class InvalidAmountException extends RuntimeException{
    public InvalidAmountException(String message) {
        super(message);
    }
}
