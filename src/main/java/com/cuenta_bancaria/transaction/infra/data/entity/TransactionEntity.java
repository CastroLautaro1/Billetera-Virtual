package com.cuenta_bancaria.transaction.infra.data.entity;

import com.cuenta_bancaria.transaction.domain.Transaction;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.antlr.v4.runtime.misc.NotNull;

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
    @Column(name = "type")
    private Transaction.TransactionType transactionType;
    @Column(name = "origin_account_id")
    private Long originAccountId;
    @Column(name = "counterparty_account_id")
    private Long counterpartyAccountId;
    private double amount;
    @Column(name = "resulting_balance")
    private double resultingBalance;
    private String details;
    private OffsetDateTime timestamp;


}
