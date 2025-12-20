package com.cuenta_bancaria.transaction.domain.port;

import com.cuenta_bancaria.transaction.domain.Transaction;

import java.time.OffsetDateTime;
import java.util.List;

public interface TransactionServicePort {

    Transaction makeTransaction(Transaction t);
    // Busca ya sea en base al originId o al counterpartyId
    List<Transaction> getAllByAccountId(Long accountId);
    Transaction getById(Long id);
    List<Transaction> filterByType(Transaction.TransactionType type);
    List<Transaction> findAllByAmountLessThan(double amount);
    List<Transaction> getHistoryByDateRange(Long accountId, OffsetDateTime start, OffsetDateTime end);

}
