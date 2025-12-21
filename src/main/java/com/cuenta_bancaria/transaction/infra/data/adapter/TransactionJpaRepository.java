package com.cuenta_bancaria.transaction.infra.data.adapter;

import com.cuenta_bancaria.transaction.domain.Transaction;
import com.cuenta_bancaria.transaction.infra.data.entity.TransactionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.OffsetDateTime;
import java.util.List;

public interface TransactionJpaRepository extends JpaRepository<TransactionEntity, Long> {

    List<TransactionEntity> findAllByTransactionType(Transaction.TransactionType transactionType);

    List<TransactionEntity> findAllByAmountLessThan(double amountIsLessThan);

    @Query("SELECT t FROM TransactionEntity t WHERE " +
            "(t.originAccountId = :id OR t.counterpartyAccountId = :id) " +
            "AND t.timestamp BETWEEN :start AND :end")
    List<TransactionEntity> findHistoryByDates(@Param("id") Long originAccountId,
                                               @Param("start") OffsetDateTime start,
                                               @Param("end") OffsetDateTime end);

    // Obtiene todas las transacciones donde este nuestro Id, ya sea como origen o destino
    @Query("SELECT t FROM TransactionEntity t WHERE t.originAccountId = :id OR t.counterpartyAccountId = :id")
    List<TransactionEntity> findByOriginAccountIdOrCounterpartyAccountId(@Param("id") Long id);
}
