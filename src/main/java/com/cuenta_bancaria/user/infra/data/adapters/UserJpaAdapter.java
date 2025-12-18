package com.cuenta_bancaria.user.infra.data.adapters;

import com.cuenta_bancaria.user.domain.User;
import com.cuenta_bancaria.user.domain.port.UserRepositoryPort;

import java.util.List;
import java.util.Optional;

public class UserJpaAdapter implements UserRepositoryPort {

    private final UserJpaRepository userRepository;

    public UserJpaAdapter(UserJpaRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User save(User user) {
        return userRepository.save(user);
    }

    @Override
    public Optional<User> findById(Long id) {
        return Optional.empty();
    }

    @Override
    public Optional<User> findByAlias(String alias) {
        return Optional.empty();
    }

    @Override
    public List<User> findAll() {
        return List.of();
    }

    @Override
    public boolean existsById(Long id) {
        return false;
    }

    @Override
    public boolean existsByAlias(String alias) {
        return false;
    }

    @Override
    public void logicallyDeleteById(Long id) {

    }
}
