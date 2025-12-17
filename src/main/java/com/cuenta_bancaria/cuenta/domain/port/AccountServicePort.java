package com.cuenta_bancaria.cuenta.domain.port;

import com.cuenta_bancaria.cuenta.domain.Account;

import java.util.List;

public interface AccountServicePort {
    Account createAccount(Account account);
    Account getAccountById(Long id);
    Account getAccountByIdUser(Long idUser);
    List<Account> getAll();
    Account updateAccount(Long id, double balance);
    void logicallyDeleteById(Long id);
}
