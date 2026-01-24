package com.billetera_virtual.transaction.domain.port;

import com.billetera_virtual.transaction.domain.Transaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

public interface TransactionRepositoryPort {

    Transaction save(Transaction t);
    Page<Transaction> getAllByAccountId(Long accountId, Pageable pageable);
    Optional<Transaction> getById(Long id);
    List<Transaction> filterByType(Transaction.TransactionType type, Long accountId);
    List<Transaction>findAllByAmountLessThan(double amount, Long accountId);
    List<Transaction> getHistoryByDateRange(Long accountId, OffsetDateTime start, OffsetDateTime end);
    Page<Transaction> findAllWithFilters(Long accountId, Transaction.TransactionType type, Double minAmount, Double maxAmount,
                                 OffsetDateTime start, OffsetDateTime end, Pageable pageable);

}
