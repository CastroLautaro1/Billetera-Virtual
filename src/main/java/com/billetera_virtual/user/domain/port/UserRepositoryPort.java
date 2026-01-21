package com.billetera_virtual.user.domain.port;

import com.billetera_virtual.user.domain.User;

import java.util.List;
import java.util.Optional;

public interface UserRepositoryPort {

    User save(User user);
    Optional<User> findById(Long id);
    Optional<User> findByEmail(String email);
    List<User> findAll();
    boolean existsById(Long id);
    boolean existsByEmail(String email);
    void logicallyDeleteById(Long id);

}
