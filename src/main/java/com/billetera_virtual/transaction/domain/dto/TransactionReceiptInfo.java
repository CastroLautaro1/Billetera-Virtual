package com.billetera_virtual.transaction.domain.dto;

import com.billetera_virtual.transaction.domain.Transaction;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

public record TransactionReceiptInfo(
        Long id,
        Transaction.TransactionType transactionType,
        BigDecimal amount,
        BigDecimal resultingBalance,
        String details,
        OffsetDateTime timestamp,
        String originFullname,
        String originCvu,
        String destinationFullname,
        String destinationCvu
) {
}
