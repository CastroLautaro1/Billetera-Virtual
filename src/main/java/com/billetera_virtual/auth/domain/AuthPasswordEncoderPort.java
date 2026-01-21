package com.billetera_virtual.auth.domain;

public interface AuthPasswordEncoderPort {

    String encodePass(String password);
}
