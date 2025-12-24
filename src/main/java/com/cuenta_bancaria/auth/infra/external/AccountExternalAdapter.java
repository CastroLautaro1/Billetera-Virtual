package com.cuenta_bancaria.auth.infra.external;

import com.cuenta_bancaria.auth.domain.AccountExternalPort;
import com.cuenta_bancaria.cuenta.domain.port.AccountServicePort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AccountExternalAdapter implements AccountExternalPort {

    private final AccountServicePort accountService;

    @Override
    public void createAccountByUserId(Long userId) {
        accountService.createAccountFromUser(userId);
    }
}
