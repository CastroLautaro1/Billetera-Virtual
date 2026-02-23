package com.billetera_virtual.transaction.infra.data.external;

import com.billetera_virtual.account.domain.port.AccountServicePort;
import com.billetera_virtual.transaction.domain.port.external.AccountExternalPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
@RequiredArgsConstructor
public class AccountExternalAdapter implements AccountExternalPort {

    private final AccountServicePort accountService;

    @Override
    public BigDecimal makeTransaction(Long originId, Long counterpartyId, BigDecimal amount) {
        return accountService.makeTransaction(originId, counterpartyId, amount);
    }

    @Override
    public Long getAccountIdByUserId(Long userId) {
        return accountService.getAccountIdByUserId(userId);
    }

    @Override
    public Long getCounterpartyAccountIdByDestination(String destination) {
        return accountService.getAccountIdByDestination(destination);
    }
}
