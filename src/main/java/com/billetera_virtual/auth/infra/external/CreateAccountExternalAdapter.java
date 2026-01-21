package com.billetera_virtual.auth.infra.external;

import com.billetera_virtual.auth.domain.CreateAccountExternalPort;
import com.billetera_virtual.account.domain.port.AccountServicePort;
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
