package com.cuenta_bancaria.auth.domain;

public interface TokenProviderPort {

    String generateToken(String email);

}
