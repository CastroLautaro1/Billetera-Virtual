package com.billetera_virtual.deposit.infra.data.adapter;

import com.billetera_virtual.deposit.domain.ports.DepositTransactionPort;
import com.billetera_virtual.transaction.domain.port.TransactionServicePort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class DepositTransactionAdapter implements DepositTransactionPort {

    private final TransactionServicePort transactionService;

    @Override
    public void registerDepositTransaction(Long accountId, BigDecimal amount, BigDecimal resultingBalance, String paymentId) {
        transactionService.createDeposit(accountId, amount, resultingBalance, paymentId);
    }

    @Override
    public boolean transactionExists(String idempotenceKey) {
        return transactionService.existsByIdempotenceKey(idempotenceKey);
    }
}
