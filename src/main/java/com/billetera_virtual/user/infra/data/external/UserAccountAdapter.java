package com.billetera_virtual.user.infra.data.external;

import com.billetera_virtual.account.domain.port.AccountServicePort;
import com.billetera_virtual.user.domain.port.external.AccountCreatorPort;
import lombok.RequiredArgsConstructor;


@RequiredArgsConstructor
public class UserAccountAdapter implements AccountCreatorPort {

    private final AccountServicePort accountService;

    @Override
    public void createInitialAccount(Long userId) {
        accountService.createAccountFromUser(userId);
    }
}
