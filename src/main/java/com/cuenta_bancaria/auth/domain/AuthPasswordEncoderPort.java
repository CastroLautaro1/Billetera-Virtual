package com.cuenta_bancaria.auth.domain;

public interface AuthPasswordEncoderPort {

    String encodePass(String password);
}
