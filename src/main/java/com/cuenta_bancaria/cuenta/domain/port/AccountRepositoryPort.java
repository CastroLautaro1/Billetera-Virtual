package com.cuenta_bancaria.cuenta.domain.port;

import com.cuenta_bancaria.cuenta.domain.Account;

import java.util.List;
import java.util.Optional;

public interface AccountRepositoryPort {

    Account save(Account account);
    Account createAccountFromUser(Long idUser);
    Optional<Account> getById(Long id);
    Optional<Account> getByIdUser(Long idUser);
    List<Account> findAll();
    boolean existsById(Long id);
    boolean existsByIdUser(Long idUser);
    void logicallyDeleteById(Long id);
}
