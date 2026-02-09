package com.billetera_virtual.transaction.domain.port;

import com.billetera_virtual.transaction.domain.Transaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.OffsetDateTime;
import java.util.List;

public interface TransactionServicePort {

    Transaction makeTransaction(Transaction t, String alias, Long userId);
    // Busca ya sea en base al originId o al counterpartyId
    Transaction getById(Long id);
    Page<Transaction> getHistory(Long accountId, Transaction.TransactionType type, Double minAmount, Double maxAmount,
                                 OffsetDateTime start, OffsetDateTime end, Pageable pageable);
    Page<Transaction> getAllTransactions(Pageable pageable);
    Page<Transaction> getAllByUserId(Long userId, Pageable pageable);

}
