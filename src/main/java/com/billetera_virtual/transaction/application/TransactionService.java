package com.billetera_virtual.transaction.application;

import com.billetera_virtual.exceptions.domain.EntityNotFoundException;
import com.billetera_virtual.transaction.domain.Transaction;
import com.billetera_virtual.transaction.domain.port.TransactionRepositoryPort;
import com.billetera_virtual.transaction.domain.port.TransactionServicePort;
import com.billetera_virtual.transaction.domain.port.external.AccountExternalPort;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class TransactionService implements TransactionServicePort {

    private final TransactionRepositoryPort transactionRepository;
    private final AccountExternalPort accountExternal;

    @Override
    @Transactional
    public Transaction makeTransaction(Transaction t, String destination, Long userId) {
        // 1. Obtengo el AccountId del usuario logueado
        Long originAccountId = accountExternal.getAccountIdByUserId(userId);

        // 1.2 Obtengo el ID de la Cuenta de destino mediante el Alias
        Long counterpartyAccountId = accountExternal.getCounterpartyAccountIdByDestination(destination);

        // 2. Uso el puerto de cuenta para registrar la transferencia
        double resultingBalance = accountExternal.makeTransaction(originAccountId, counterpartyAccountId, t.getAmount());

        // 3. Completo la informacion de los campos que faltan
        t.setTransactionType(Transaction.TransactionType.TRANSFER);
        t.setOriginAccountId(originAccountId);
        t.setCounterpartyAccountId(counterpartyAccountId);
        t.setResultingBalance(resultingBalance);
        t.setTimestamp(OffsetDateTime.now());

        return transactionRepository.save(t);
    }

    @Override
    public Transaction getById(Long id) {
        Optional<Transaction> transactionOpt = transactionRepository.getById(id);
        Transaction transaction;

        if (transactionOpt.isPresent()) {
            transaction = transactionOpt.get();
        }
        else {
            throw  new EntityNotFoundException("El ID de la transaccion no existe");
        }
        return transaction;
    }

    @Override
    public Page<Transaction> getHistory(Long accountId, Transaction.TransactionType type, Double minAmount, Double maxAmount,
                                        OffsetDateTime start, OffsetDateTime end, Pageable pageable) {


        return transactionRepository.findAllWithFilters(accountId, type, minAmount, maxAmount, start, end, pageable);
    }
}
