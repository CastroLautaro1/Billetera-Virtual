package com.billetera_virtual.auth.infra.external;

import com.billetera_virtual.auth.domain.TokenProviderPort;
import com.billetera_virtual.security.application.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class JwtTokenAdapter implements TokenProviderPort {

    private final JwtService jwtService;

    @Override
    public String generateToken(String email) {
        return jwtService.generateToken(email);
    }
}
