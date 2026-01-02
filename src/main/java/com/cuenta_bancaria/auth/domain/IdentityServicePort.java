package com.cuenta_bancaria.auth.domain;

public interface IdentityServicePort {

    void authenticate(String email, String password);

}
