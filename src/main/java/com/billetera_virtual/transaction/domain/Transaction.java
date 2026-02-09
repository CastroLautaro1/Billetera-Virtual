package com.billetera_virtual.transaction.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Transaction {

    private Long id;
    private TransactionType transactionType;
    private Long originAccountId;
    private Long counterpartyAccountId;
    private BigDecimal amount;
    private BigDecimal resultingBalance;
    private String details;
    private OffsetDateTime timestamp;

    public enum TransactionType {
        DEPOSIT, TRANSFER, WITHDRAWAL
    }

}
