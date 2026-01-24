package com.billetera_virtual.transaction.infra.data.adapter;

import com.billetera_virtual.transaction.domain.Transaction;
import com.billetera_virtual.transaction.domain.port.TransactionRepositoryPort;
import com.billetera_virtual.transaction.infra.data.entity.TransactionEntity;
import com.billetera_virtual.transaction.infra.data.mapper.TransactionMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class TransactionJpaAdapter implements TransactionRepositoryPort {

    private final TransactionJpaRepository jpaRepository;
    private final TransactionMapper transactionMapper;

    @Override
    public Transaction save(Transaction t) {
        TransactionEntity entity = transactionMapper.toEntity(t);
        TransactionEntity saved = jpaRepository.save(entity);
        return transactionMapper.toDomain(saved);
    }

    @Override
    public Page<Transaction> getAllByAccountId(Long accountId, Pageable pageable) {
        return jpaRepository.findAllByAccountId(accountId, pageable)
                .map(transactionMapper::toDomain);
    }

    @Override
    public Optional<Transaction> getById(Long id) {
        return jpaRepository.findById(id)
                .map(transactionMapper::toDomain);
    }

    @Override
    public List<Transaction> filterByType(Transaction.TransactionType type, Long accountId) {
        List<TransactionEntity> list = jpaRepository.findAllByTransactionType(type, accountId);
        return list.stream()
                .map(transactionMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Transaction> findAllByAmountLessThan(double amount, Long accountId) {
        List<TransactionEntity> list = jpaRepository.findAllByAmountLessThan(amount, accountId);
        return list.stream()
                .map(transactionMapper::toDomain)
                .collect(Collectors.toList());
    }

    // Revisar la logica de este metodo
    @Override
    public List<Transaction> getHistoryByDateRange(Long accountId, OffsetDateTime start, OffsetDateTime end) {
        List<TransactionEntity> list = jpaRepository.findHistoryByDates(accountId, start, end);
        return list.stream()
                .map(transactionMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public Page<Transaction> findAllWithFilters(Long accountId, Transaction.TransactionType type, Double minAmount, Double maxAmount,
                                                OffsetDateTime start, OffsetDateTime end, Pageable pageable) {
        Page<TransactionEntity> transactionEntities = jpaRepository.findAllWithFilters(accountId, type, minAmount, maxAmount,
                start, end, pageable);

        return transactionEntities.map(transactionMapper::toDomain);
    }
}
