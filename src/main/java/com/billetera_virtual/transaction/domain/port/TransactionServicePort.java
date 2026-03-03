package com.billetera_virtual.transaction.domain.port;

import com.billetera_virtual.transaction.domain.Transaction;
import com.billetera_virtual.transaction.domain.dto.TransactionAccountInfo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.OffsetDateTime;
import java.util.List;

public interface TransactionServicePort {

    Transaction makeTransaction(Transaction t, String destination, Long userId);
    byte [] generateReceipt(Long id);

    Transaction getById(Long id, Long accountId, String role);
    Page<Transaction> getHistory(Long accountId, Transaction.TransactionType type, Double minAmount, Double maxAmount,
                                 OffsetDateTime start, OffsetDateTime end, Pageable pageable);
    Page<Transaction> getAllTransactions(Pageable pageable);
    Page<Transaction> getAllByUserId(Long userId, Pageable pageable);

}
