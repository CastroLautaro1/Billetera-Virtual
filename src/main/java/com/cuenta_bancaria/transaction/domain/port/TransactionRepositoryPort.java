package com.cuenta_bancaria.transaction.domain.port;

import com.cuenta_bancaria.transaction.domain.Transaction;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

public interface TransactionRepositoryPort {

    Transaction save(Transaction t);
    List<Transaction> getAllByAccountId(Long accountId);
    Optional<Transaction> getById(Long id);
    List<Transaction> filterByType(Transaction.TransactionType type);
    List<Transaction>findAllByAmountLessThan(double amount);
    List<Transaction> getHistoryByDateRange(Long accountId, OffsetDateTime start, OffsetDateTime end);

}
