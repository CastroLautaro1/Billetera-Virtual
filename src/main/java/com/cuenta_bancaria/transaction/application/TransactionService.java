package com.cuenta_bancaria.transaction.application;

import com.cuenta_bancaria.exceptions.domain.EntityNotFoundException;
import com.cuenta_bancaria.transaction.domain.Transaction;
import com.cuenta_bancaria.transaction.domain.port.TransactionRepositoryPort;
import com.cuenta_bancaria.transaction.domain.port.TransactionServicePort;
import com.cuenta_bancaria.transaction.domain.port.external.AccountExternalPort;
import com.cuenta_bancaria.transaction.domain.port.external.UserExternalPort;
import com.cuenta_bancaria.transaction.infra.web.dto.TransactionDTO;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class TransactionService implements TransactionServicePort {

    private final TransactionRepositoryPort transactionRepository;
    private final AccountExternalPort accountExternal;
    private final UserExternalPort userExternal;

    @Override
    @Transactional
    public Transaction makeTransaction(Transaction t, String alias, Long userId) {
        // 1. Obtengo el AccountId del usuario logueado
        Long originAccountId = accountExternal.getAccountIdByUserId(userId);

        // 1.2 Obtengo el UserId de destino mediante el Alias
        Long counterpartyUserId = userExternal.getUserIdByAlias(alias);

        // 1.3 Obtengo el ID de la Cuenta de destino
        Long counterpartyAccountId = accountExternal.getAccountIdByUserId(counterpartyUserId);

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
    public List<Transaction> getAllByAccountId(Long accountId) {
        return transactionRepository.getAllByAccountId(accountId);
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
    public List<Transaction> filterByType(Transaction.TransactionType type, Long accountId) {
        if (type == null) {
            throw new IllegalArgumentException("El tipo de transaccion no puede ser nulo");
        }
        return transactionRepository.filterByType(type, accountId);
    }

    @Override
    public List<Transaction> findAllByAmountLessThan(double amount, Long accountId) {
        return transactionRepository.findAllByAmountLessThan(amount, accountId);
    }

    @Override
    public List<Transaction> getHistoryByDateRange(Long accountId, OffsetDateTime start, OffsetDateTime end) {
        return List.of();
    }
}
