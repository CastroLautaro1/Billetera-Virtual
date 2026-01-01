package com.cuenta_bancaria.transaction.domain.port;

import com.cuenta_bancaria.transaction.domain.Transaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.OffsetDateTime;
import java.util.List;

public interface TransactionServicePort {

    Transaction makeTransaction(Transaction t, String alias, Long userId);
    // Busca ya sea en base al originId o al counterpartyId
    Page<Transaction> getAllByAccountId(Long accountId, Pageable pageable);
    Transaction getById(Long id);
    List<Transaction> filterByType(Transaction.TransactionType type, Long accountId);
    List<Transaction> findAllByAmountLessThan(double amount, Long accountId);
    List<Transaction> getHistoryByDateRange(Long accountId, OffsetDateTime start, OffsetDateTime end);

}
