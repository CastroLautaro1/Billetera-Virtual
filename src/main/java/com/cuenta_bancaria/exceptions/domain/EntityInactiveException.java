package com.cuenta_bancaria.exceptions.domain;

public class EntityInactiveException extends RuntimeException{
    public EntityInactiveException(String message) {
        super(message);
    }
}
