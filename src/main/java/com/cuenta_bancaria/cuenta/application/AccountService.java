package com.cuenta_bancaria.cuenta.application;

import com.cuenta_bancaria.cuenta.domain.Account;
import com.cuenta_bancaria.cuenta.domain.port.AccountRepositoryPort;
import com.cuenta_bancaria.cuenta.domain.port.AccountServicePort;
import com.cuenta_bancaria.exceptions.domain.*;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AccountService implements AccountServicePort {

    private final AccountRepositoryPort accountRepository;

    public AccountService(AccountRepositoryPort accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Override
    public Account createAccount(Account account) {

        // agregar validacion que verifique si el idUser existe

        if(accountRepository.existsByIdUser(account.getUser_id())) {
            throw new EntityAlreadyExistsException("El ID de Usuario ya esta asociado a una cuenta, no puede tener otra");
        }

        if(account.getBalance() < 0) {
            throw new InsufficientBalanceException("El saldo de la cuenta no puede ser inferior a 0");
        }

        account.setStatus(true);

        return accountRepository.save(account);
    }

    @Override
    public Account createAccountFromUser(Long userId) {

        if(accountRepository.existsByIdUser(userId)) {
            throw new EntityAlreadyExistsException("El ID de Usuario ya esta asociado a una cuenta, no puede tener otra");
        }

        // Recibo el Id del Usuario y creo la cuenta correspondiente
        Account account = Account.builder()
                .user_id(userId)
                .balance(0)
                .status(true)
                .build();

        return accountRepository.save(account);
    }

    @Override
    @Transactional
    public double makeTransaction(Long originAccountId, Long counterpartyAccountId, double amount) {

        if (amount <= 0) {
            throw new InvalidAmountException("El monto no puede ser igual o menor a 0");
        }

        if (originAccountId.equals(counterpartyAccountId)) {
            throw new IllegalArgumentException("La cuenta de origen y destino no pueden ser la misma");
        }

        // Obtengo la cuenta de origen, usando el user_id
        Account origin = getAccountById(originAccountId);

        if (!origin.isStatus()) {
            throw new EntityInactiveException("La cuenta de origen se encuentra deshabilitada");
        }

        if (origin.getBalance() < amount) {
            throw new InsufficientBalanceException("Saldo insuficiente en la cuenta. Saldo: " + origin.getBalance() + ", Monto: " + amount);
        }

        // Obtengo la cuenta de la contraparte, usando su user_id
        Account counterparty = getAccountById(counterpartyAccountId);

        if(!counterparty.isStatus()) {
            throw new EntityInactiveException("La cuenta de destino se encuentra deshabilitada");
        }

        // Realizo la operacion en el balance de ambas cuentas
        origin.setBalance(origin.getBalance() - amount);
        counterparty.setBalance(counterparty.getBalance() + amount);

        accountRepository.save(origin);
        accountRepository.save(counterparty);

        return origin.getBalance();
    }

    @Override
    public Long getAccountIdByUserId(Long userId) {
        return getAccountByIdUser(userId).getId();
    }

    @Override
    public Account getAccountById(Long id) {
        Optional<Account> accountOpt = accountRepository.getById(id);
        Account account;

        if (accountOpt.isPresent()) {
            account = accountOpt.get();
        }
        else {
            throw new EntityNotFoundException("El ID ingresado no coincide con ninguna cuenta");
        }

        return account;
    }

    @Override
    public Account getAccountByIdUser(Long idUser) {
        Optional<Account> accountOpt = accountRepository.getByIdUser(idUser);
        Account account;

        if (accountOpt.isPresent()) {
            account = accountOpt.get();
        }
        else {
            throw new EntityNotFoundException("El ID ingresado no coincide con ninguna cuenta");
        }

        return account;
    }

    @Override
    public List<Account> getAll() {
        return accountRepository.findAll();
    }

    @Override
    public void logicallyDeleteById(Long id) {
        boolean exists = accountRepository.existsById(id);

        if(exists) {
            accountRepository.logicallyDeleteById(id);
        }
        else {
            throw new EntityNotFoundException("El ID ingresado no coincide con ninguna cuenta");
        }
    }

    @Override
    public Account updateAccount(Long id, double balance) {

        // hacer validacion que verifique que el ID pertenece al Usuario logueado

        Account account = getAccountById(id);

        if (!account.isStatus()) {
            throw new EntityInactiveException("La cuenta se encuentra inhabilitada");
        }

        if(balance < 0) {
            throw new InsufficientBalanceException("El saldo de la cuenta no puede ser inferior a 0");
        }

        account.setBalance(balance);

        return accountRepository.save(account);
    }
}
