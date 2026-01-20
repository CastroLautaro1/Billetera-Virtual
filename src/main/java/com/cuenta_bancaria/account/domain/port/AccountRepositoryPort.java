package com.cuenta_bancaria.account.domain.port;

import com.cuenta_bancaria.account.domain.Account;

import java.util.List;
import java.util.Optional;

public interface AccountRepositoryPort {

    Account save(Account account);
    Account createAccountFromUser(Long idUser);
    Optional<Account> getById(Long id);
    Optional<Account> getByIdUser(Long idUser);
    Optional<Account> getAccountByAlias(String alias);
    Optional<Account> getAccountByCvu(String cvu);
    List<Account> findAll();
    boolean existsById(Long id);
    boolean existsByIdUser(Long idUser);
    boolean existsByAlias(String alias);
    void logicallyDeleteById(Long id);
}
