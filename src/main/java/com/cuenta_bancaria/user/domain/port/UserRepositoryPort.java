package com.cuenta_bancaria.user.domain.port;

import com.cuenta_bancaria.user.domain.User;

import java.util.List;
import java.util.Optional;

public interface UserRepositoryPort {

    User save(User user);
    Optional<User> findById(Long id);
    Optional<User> findByEmail(String email);
    Optional<User> findByAlias(String alias);
    List<User> findAll();
    boolean existsById(Long id);
    boolean existsByEmail(String email);
    boolean existsByAlias(String alias);
    void logicallyDeleteById(Long id);

}
