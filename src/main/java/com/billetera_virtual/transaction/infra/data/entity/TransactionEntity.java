package com.billetera_virtual.transaction.infra.data.entity;

import com.billetera_virtual.transaction.domain.Transaction;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;

@Entity
@Table(name = "transaction")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TransactionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private Transaction.TransactionType transactionType;

    @Column(name = "origin_account_id")
    private Long originAccountId;

    @Column(name = "counterparty_account_id")
    private Long counterpartyAccountId;

    @Column(nullable = false)
    private double amount;

    @Column(name = "resulting_balance", nullable = false)
    private double resultingBalance;

    @Column(nullable = false, length = 100)
    private String details;

    @Column(nullable = false, columnDefinition = "TIMESTAMP WITH TIME ZONE")
    private OffsetDateTime timestamp;


}
