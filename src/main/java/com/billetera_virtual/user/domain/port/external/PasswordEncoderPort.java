package com.billetera_virtual.user.domain.port.external;

public interface PasswordEncoderPort {
    String encode(String rawPassword);
}
