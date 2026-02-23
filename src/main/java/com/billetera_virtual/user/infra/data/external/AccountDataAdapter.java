package com.billetera_virtual.user.infra.data.external;

import com.billetera_virtual.account.domain.port.AccountServicePort;
import com.billetera_virtual.user.domain.port.external.AccountDataExternalPort;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class AccountDataAdapter implements AccountDataExternalPort {

    private final AccountServicePort accountServicePort;

    // Obtengo el User Id
    @Override
    public Long getUserIdByAccountId(Long accountId) {
        return accountServicePort.getAccountById(accountId).getUser_id();
    }
}
