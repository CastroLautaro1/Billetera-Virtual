package com.cuenta_bancaria.auth.infra.external;

import com.cuenta_bancaria.auth.domain.TokenProviderPort;
import com.cuenta_bancaria.security.application.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
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
