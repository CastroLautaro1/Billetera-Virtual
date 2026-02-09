package com.billetera_virtual.user.domain.port;

import com.billetera_virtual.user.domain.User;

import java.util.List;
import java.util.Optional;

public interface UserRepositoryPort {

    User save(User user);

    List<User> findAll();
    Optional<User> findById(Long id);
    Optional<User> findByEmail(String email);

    boolean existsById(Long id);
    boolean existsByEmail(String email);

    void logicallyDeleteById(Long id);

}
