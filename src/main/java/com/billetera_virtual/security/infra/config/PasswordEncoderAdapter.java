package com.billetera_virtual.security.infra.config;

import com.billetera_virtual.auth.domain.AuthPasswordEncoderPort;
import com.billetera_virtual.user.domain.port.external.PasswordEncoderPort;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PasswordEncoderAdapter implements PasswordEncoderPort, AuthPasswordEncoderPort {

    private final PasswordEncoder passwordEncoder;

    @Override
    public String encode(String rawPassword) {
        return passwordEncoder.encode(rawPassword);
    }

    @Override
    public String encodePass(String password) {
        return passwordEncoder.encode(password);
    }
}
