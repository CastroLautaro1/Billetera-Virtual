package com.cuenta_bancaria.auth.infra.external;

import com.cuenta_bancaria.auth.domain.CreateAccountExternalPort;
import com.cuenta_bancaria.account.domain.port.AccountServicePort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CreateAccountExternalAdapter implements CreateAccountExternalPort {

    private final AccountServicePort accountService;

    @Override
    public void createAccountByUserId(Long userId) {
        accountService.createAccountFromUser(userId);
    }
}
