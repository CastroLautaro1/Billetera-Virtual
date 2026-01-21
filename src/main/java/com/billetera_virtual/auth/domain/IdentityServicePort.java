package com.billetera_virtual.auth.domain;

public interface IdentityServicePort {

    void authenticate(String email, String password);

}
