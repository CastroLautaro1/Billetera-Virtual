package com.billetera_virtual.transaction.application;

import com.billetera_virtual.exceptions.domain.AccessDeniedException;
import com.billetera_virtual.exceptions.domain.EntityNotFoundException;
import com.billetera_virtual.transaction.domain.Transaction;
import com.billetera_virtual.transaction.domain.dto.TransactionAccountInfo;
import com.billetera_virtual.transaction.domain.dto.TransactionReceiptInfo;
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
import java.util.Optional;

@RequiredArgsConstructor
public class TransactionService implements TransactionServicePort {

    private final TransactionRepositoryPort transactionRepository;
    private final AccountExternalPort accountExternal;
    private final ReceiptGeneratorPort receiptGenerator;

    @Override
    @Transactional
    public Transaction makeTransaction(Transaction t, String destination, Long userId, String idempotencyKey) {
        Optional<Transaction> existingTransaction = transactionRepository.findByIdempotencyKey(idempotencyKey);
        if(existingTransaction.isPresent()) {
            return existingTransaction.get();
        }

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
        t.setIdempotencyKey(idempotencyKey);

        return transactionRepository.save(t);
    }

    @Transactional
    @Override
    public void createDeposit(Long accountId, BigDecimal amount, BigDecimal resultingBalance, String idempotenceKey) {
        Transaction tx = new Transaction();

        tx.setTransactionType(Transaction.TransactionType.DEPOSIT);
        tx.setCounterpartyAccountId(accountId);
        tx.setAmount(amount);
        tx.setResultingBalance(resultingBalance);
        tx.setDetails("Deposito desde Mercado Pago");
        tx.setTimestamp(OffsetDateTime.now());
        tx.setIdempotencyKey(idempotenceKey);

        transactionRepository.save(tx);
    }

    @Override
    public byte[] generateReceipt(Long id) {
        Transaction tx = transactionRepository.getById(id)
                .orElseThrow(() -> new EntityNotFoundException("Transaccion no encontrada"));

        TransactionAccountInfo origin = accountExternal.getAccountReceipt(tx.getOriginAccountId());
        TransactionAccountInfo destination = accountExternal.getAccountReceipt(tx.getCounterpartyAccountId());

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
        boolean isOrigin = transaction.getOriginAccountId() != null
                && transaction.getOriginAccountId().equals(accountId);

        boolean isCounterparty = transaction.getCounterpartyAccountId() != null
                && transaction.getCounterpartyAccountId().equals(accountId);


        if (!isOrigin && !isCounterparty) {
            throw new AccessDeniedException("La transaccion no pertenece al usuario.");
        }

        return transaction;
    }

    @Override
    public TransactionReceiptInfo getTransactionReceipt(Long transactionId, Long accountId, String role) {
        Transaction transaction = getById(transactionId, accountId, role);

        TransactionAccountInfo originInfo;

        if(transaction.getTransactionType() == Transaction.TransactionType.TRANSFER) {
            originInfo = accountExternal.getAccountReceipt(transaction.getOriginAccountId());
        } else {
            originInfo = new TransactionAccountInfo("Mercado Pago", "-", "-");
        }

        TransactionAccountInfo destinationInfo = accountExternal.getAccountReceipt(transaction.getCounterpartyAccountId());

        return new TransactionReceiptInfo(
                transaction.getId(),
                transaction.getTransactionType(),
                transaction.getAmount(),
                transaction.getResultingBalance(),
                transaction.getDetails(),
                transaction.getTimestamp(),
                originInfo.fullname(),
                originInfo.cvu(),
                destinationInfo.fullname(),
                destinationInfo.cvu()
        );
    }

    @Override
    public Page<Transaction> getHistory(Long accountId, Transaction.TransactionType type, Double minAmount, Double maxAmount,
                                        OffsetDateTime start, OffsetDateTime end, String name, Pageable pageable) {

        String cleanName = (name != null && !name.trim().isEmpty()) ? name.trim() : null;
        return transactionRepository.findAllWithFilters(accountId, type, minAmount, maxAmount, start, end, cleanName, pageable);
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

    @Override
    public boolean existsByIdempotenceKey(String idempotenceKey) {
        return transactionRepository.existsByIdempotenceKey(idempotenceKey);
    }
}
