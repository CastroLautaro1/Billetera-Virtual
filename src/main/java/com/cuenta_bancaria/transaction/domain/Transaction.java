package com.cuenta_bancaria.transaction.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Transaction {

    private Long id;
    private Type type;
    private Long originAccountId;
    private Long counterpartyAccountId;
    private double amount;
    private double resultingBalance;
    private String details;
    private OffsetDateTime timestamp;

    public enum Type {
        DEPOSIT, TRANSFER, WITHDRAWAL
    }

}
