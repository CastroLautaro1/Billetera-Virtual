package com.billetera_virtual.transaction.infra.data.adapter;

import com.billetera_virtual.transaction.domain.Transaction;
import com.billetera_virtual.transaction.infra.data.entity.TransactionEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.OffsetDateTime;
import java.util.List;

public interface TransactionJpaRepository extends JpaRepository<TransactionEntity, Long> {

    // Obtiene todas las transacciones donde este nuestro Id, ya sea como origen o destino
    @Query("SELECT t FROM TransactionEntity t WHERE " +
           "(t.originAccountId = :id OR t.counterpartyAccountId = :id)")
    Page<TransactionEntity> findAllByAccountId(@Param("id") Long accountId, Pageable pageable);

    @Query("SELECT t FROM TransactionEntity t WHERE " +
            "(t.originAccountId = :id OR t.counterpartyAccountId = :id) " +
            "AND t.transactionType = :type")
    List<TransactionEntity> findAllByTransactionType(@Param("type") Transaction.TransactionType transactionType,
                                                     @Param("id") Long id);

    @Query("SELECT t FROM TransactionEntity t WHERE " +
           "(t.originAccountId = :id OR t.counterpartyAccountId = :id) " +
           "AND t.amount <= :amount")
    List<TransactionEntity> findAllByAmountLessThan(@Param("amount") double amount,
                                                    @Param("id") Long id);

    @Query("SELECT t FROM TransactionEntity t WHERE " +
            "(t.originAccountId = :id OR t.counterpartyAccountId = :id) " +
            "AND t.timestamp BETWEEN :start AND :end")
    List<TransactionEntity> findHistoryByDates(@Param("id") Long originAccountId,
                                               @Param("start") OffsetDateTime start,
                                               @Param("end") OffsetDateTime end);



}
