package com.cuenta_bancaria.account.domain.port;

import com.cuenta_bancaria.account.domain.Account;

import java.util.List;

public interface AccountServicePort {
    Account createAccount(Account account);
    Account getAccountById(Long id);
    Account getAccountByIdUser(Long idUser);
    Account getAccountByAlias(String alias);
    Account getAccountByCvu(String cvu);
    List<Account> getAll();
    Account updateAccount(Long id, double balance);
    void updateAlias(Long id, String alias);
    void logicallyDeleteById(Long id);
    Account createAccountFromUser(Long userId);
    double makeTransaction(Long originId, Long counterpartyId, double amount);
    Long getAccountIdByUserId(Long userId);
    Long getAccountIdByAlias(String alias);

}
