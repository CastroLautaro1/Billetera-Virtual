package com.billetera_virtual.deposit.domain.ports;

import java.math.BigDecimal;

public interface DepositTransactionPort {

    void registerDepositTransaction(Long accountId, BigDecimal amount, BigDecimal resultingBalance, String paymentId);
    boolean transactionExists(String idempotenceKey);
}
