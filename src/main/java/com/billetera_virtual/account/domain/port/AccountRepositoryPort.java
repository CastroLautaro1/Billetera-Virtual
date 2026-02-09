package com.billetera_virtual.account.domain.port;

import com.billetera_virtual.account.domain.Account;

import java.util.List;
import java.util.Optional;

public interface AccountRepositoryPort {

    Account save(Account account);
    Account createAccountFromUser(Long idUser);

    List<Account> findAll();
    Optional<Account> getById(Long id);
    Optional<Account> getByIdUser(Long idUser);
    Optional<Account> getAccountByAlias(String alias);
    Optional<Account> getAccountByCvu(String cvu);


    boolean existsById(Long id);
    boolean existsByIdUser(Long idUser);
    boolean existsByAlias(String alias);

    void logicallyDeleteById(Long id);
}
