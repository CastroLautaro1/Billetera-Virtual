package com.cuenta_bancaria.cuenta.application;

import com.cuenta_bancaria.cuenta.domain.Account;
import com.cuenta_bancaria.cuenta.domain.port.AccountRepositoryPort;
import com.cuenta_bancaria.cuenta.domain.port.AccountServicePort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AccountService implements AccountServicePort {

    private AccountRepositoryPort accountRepository;

    public AccountService(AccountRepositoryPort accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Override
    public Account createAccount(Account account) {

        // agregar validacion que verifique si el idUser existe

        if(accountRepository.existsByIdUser(account.getUser_id())) {
            throw new RuntimeException("El ID de Usuario ya esta asociado a una cuenta, no puede tener otra");
        }

        if(account.getBalance() < 0) {
            throw new IllegalArgumentException("El saldo de la cuenta no puede ser inferior a 0");
        }

        account.setStatus(true);

        return accountRepository.save(account);
    }

    @Override
    public Account getAccountById(Long id) {
        Optional<Account> accountOpt = accountRepository.getById(id);
        Account account;

        if (accountOpt.isPresent()) {
            account = accountOpt.get();
        }
        else {
            throw new RuntimeException("El ID ingresado no coincide con ninguna cuenta");
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
            throw new RuntimeException("El ID ingresado no coincide con ninguna cuenta");
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
            throw new RuntimeException("El ID ingresado no coincide con ninguna cuenta");
        }
    }

    @Override
    public Account updateAccount(Long id, double balance) {

        // hacer validacion que verifique que el ID pertenece al Usuario logueado

        Account account = getAccountById(id);

        if (!account.isStatus()) {
            throw new RuntimeException("La cuenta se encuentra inhabilitada");
        }

        if(balance < 0) {
            throw new IllegalArgumentException("El saldo de la cuenta no puede ser inferior a 0");
        }

        account.setBalance(balance);

        return accountRepository.save(account);
    }
}
