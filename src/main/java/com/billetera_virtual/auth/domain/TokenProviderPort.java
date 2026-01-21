package com.billetera_virtual.auth.domain;

public interface TokenProviderPort {

    String generateToken(String email);

}
