package com.cuenta_bancaria.user.infra.data.adapters;

import com.cuenta_bancaria.user.domain.User;
import com.cuenta_bancaria.user.domain.port.UserRepositoryPort;
import com.cuenta_bancaria.user.infra.data.entity.UserEntity;
import com.cuenta_bancaria.user.infra.data.mapper.UserMapper;
import jakarta.transaction.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class UserJpaAdapter implements UserRepositoryPort {

    private final UserJpaRepository userRepository;

    public UserJpaAdapter(UserJpaRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User save(User user) {
        UserEntity entity = UserMapper.toEntity(user);
        UserEntity saved = userRepository.save(entity);
        return UserMapper.toDomain(saved);
    }

    @Override
    public Optional<User> findById(Long id) {
        return userRepository.findById(id)
                .map(UserMapper::toDomain);
    }

    @Override
    public Optional<User> findByAlias(String alias) {
        return userRepository.findByAlias(alias)
                .map(UserMapper::toDomain);
    }

    @Override
    public List<User> findAll() {
        return userRepository.findAll()
                .stream()
                .map(UserMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public boolean existsById(Long id) {
        return userRepository.existsById(id);
    }

    @Override
    public boolean existsByAlias(String alias) {
        return userRepository.existsByAlias(alias);
    }

    @Override
    @Transactional
    public void logicallyDeleteById(Long id) {
        userRepository.logicallyDeleteById(id);
    }
}
