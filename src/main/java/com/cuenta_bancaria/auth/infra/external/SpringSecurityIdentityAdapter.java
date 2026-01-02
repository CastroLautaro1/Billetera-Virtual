package com.cuenta_bancaria.auth.infra.external;

import com.cuenta_bancaria.auth.domain.IdentityServicePort;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SpringSecurityIdentityAdapter implements IdentityServicePort {

    private final AuthenticationManager authenticationManager;

    @Override
    public void authenticate(String email, String password) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(email, password)
        );
    }
}
