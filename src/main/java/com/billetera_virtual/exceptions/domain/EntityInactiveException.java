package com.billetera_virtual.exceptions.domain;

public class EntityInactiveException extends RuntimeException{
    public EntityInactiveException(String message) {
        super(message);
    }
}
