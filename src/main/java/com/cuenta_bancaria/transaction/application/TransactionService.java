package com.cuenta_bancaria.transaction.application;

import com.cuenta_bancaria.transaction.domain.Transaction;
import com.cuenta_bancaria.transaction.domain.port.TransactionRepositoryPort;
import com.cuenta_bancaria.transaction.domain.port.TransactionServicePort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TransactionService implements TransactionServicePort {

    private final TransactionRepositoryPort transactionRepository;

    @Override
    public Transaction makeTransaction(Transaction t) {
        return null;
    }

    @Override
    public List<Transaction> getAllByAccountId(Long accountId) {
        return List.of();
    }

    @Override
    public Transaction getById(Long id) {
        return null;
    }

    @Override
    public List<Transaction> filterByType(Transaction.TransactionType type) {
        return List.of();
    }

    @Override
    public List<Transaction> findAllByAmountLessThan(double amount) {
        return List.of();
    }

    @Override
    public List<Transaction> getHistoryByDateRange(Long accountId, OffsetDateTime start, OffsetDateTime end) {
        return List.of();
    }
}
