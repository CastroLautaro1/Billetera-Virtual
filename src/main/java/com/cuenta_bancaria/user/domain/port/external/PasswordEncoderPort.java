package com.cuenta_bancaria.user.domain.port.external;

public interface PasswordEncoderPort {
    String encode(String rawPassword);
}
