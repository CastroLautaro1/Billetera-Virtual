package com.billetera_virtual.transaction.application;

import com.billetera_virtual.exceptions.domain.AccessDeniedException;
import com.billetera_virtual.exceptions.domain.EntityNotFoundException;
import com.billetera_virtual.transaction.domain.Transaction;
import com.billetera_virtual.transaction.domain.dto.TransactionAccountInfo;
import com.billetera_virtual.transaction.domain.port.TransactionRepositoryPort;
import com.billetera_virtual.transaction.domain.port.TransactionServicePort;
import com.billetera_virtual.transaction.domain.port.external.AccountExternalPort;
import com.billetera_virtual.transaction.domain.port.external.ReceiptGeneratorPort;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

@RequiredArgsConstructor
public class TransactionService implements TransactionServicePort {

    private final TransactionRepositoryPort transactionRepository;
    private final AccountExternalPort accountExternal;
    private final ReceiptGeneratorPort receiptGenerator;

    @Override
    @Transactional
    public Transaction makeTransaction(Transaction t, String destination, Long userId) {
        // 1. Obtengo el AccountId del usuario logueado
        Long originAccountId = accountExternal.getAccountIdByUserId(userId);

        // 1.2 Obtengo el ID de la Cuenta de destino mediante el Alias
        Long counterpartyAccountId = accountExternal.getCounterpartyAccountIdByDestination(destination);

        // 2. Uso el puerto de cuenta para registrar la transferencia
        BigDecimal resultingBalance = accountExternal.makeTransaction(originAccountId, counterpartyAccountId, t.getAmount());

        // 3. Completo la informacion de los campos que faltan
        t.setTransactionType(Transaction.TransactionType.TRANSFER);
        t.setOriginAccountId(originAccountId);
        t.setCounterpartyAccountId(counterpartyAccountId);
        t.setResultingBalance(resultingBalance);
        t.setTimestamp(OffsetDateTime.now());

        return transactionRepository.save(t);
    }

    @Override
    public byte[] generateReceipt(Long id) {
        Transaction tx = transactionRepository.getById(id)
                .orElseThrow(() -> new EntityNotFoundException("Transaccion no encontrada"));

        TransactionAccountInfo origin = accountExternal.getAccountDataById(tx.getOriginAccountId());
        TransactionAccountInfo destination = accountExternal.getAccountDataById(tx.getCounterpartyAccountId());

        return receiptGenerator.generateReceipt(tx, origin, destination);
    }

    @Override
    public Transaction getById(Long id, Long accountId, String role) {
        Transaction transaction = transactionRepository.getById(id)
                .orElseThrow(() -> new EntityNotFoundException("El ID ingresado no coincide con ninguna transaccion"));

        // Si el usuario es Admin entonces puede obtener la transaccion
        if (role.equals("ADMIN")) {
            return transaction;
        }

        // Si no es Admin, valido que la transaccion le pertenesca, ya sea como origen o contraparte
        if (!transaction.getOriginAccountId().equals(accountId)
                && !transaction.getCounterpartyAccountId().equals(accountId)) {
            throw new AccessDeniedException("La transaccion no pertenece al usuario.");
        }

        return transaction;
    }

    @Override
    public Page<Transaction> getHistory(Long accountId, Transaction.TransactionType type, Double minAmount, Double maxAmount,
                                        OffsetDateTime start, OffsetDateTime end, Pageable pageable) {


        return transactionRepository.findAllWithFilters(accountId, type, minAmount, maxAmount, start, end, pageable);
    }

    @Override
    public Page<Transaction> getAllTransactions(Pageable pageable) {
        return transactionRepository.findAll(pageable);
    }

    @Override
    public Page<Transaction> getAllByUserId(Long userId, Pageable pageable) {
        // Obtengo el AccountId mediante el UserId
        Long accountId = accountExternal.getAccountIdByUserId(userId);

        return transactionRepository.getAllByAccountId(accountId, pageable);
    }
}
