package com.cuenta_bancaria.transaction.infra.data.external;

import com.cuenta_bancaria.cuenta.domain.port.AccountServicePort;
import com.cuenta_bancaria.transaction.domain.port.external.AccountExternalPort;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class AccountExternalAdapter implements AccountExternalPort {

    private final AccountServicePort accountService;

    @Override
    public double makeTransaction(Long originId, Long counterpartyId, double amount) {
        return accountService.makeTransaction(originId, counterpartyId, amount);
    }
}
