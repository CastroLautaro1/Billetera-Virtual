package com.cuenta_bancaria.user.infra.data.external;

import com.cuenta_bancaria.cuenta.domain.port.AccountServicePort;
import com.cuenta_bancaria.user.domain.port.external.AccountCreatorPort;
import lombok.RequiredArgsConstructor;


@RequiredArgsConstructor
public class UserAccountAdapter implements AccountCreatorPort {

    private final AccountServicePort accountService;

    @Override
    public void createInitialAccount(Long userId) {
        accountService.createAccountFromUser(userId);
    }
}
