package com.cuenta_bancaria.auth.infra.dto;


public record RegisterRequest (
        String firstname,
        String lastname,
        String alias,
        String email,
        String password
) {}
