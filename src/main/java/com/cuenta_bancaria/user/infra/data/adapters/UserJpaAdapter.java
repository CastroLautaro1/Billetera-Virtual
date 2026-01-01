package com.cuenta_bancaria.user.infra.data.adapters;

import com.cuenta_bancaria.user.domain.User;
import com.cuenta_bancaria.user.domain.port.UserRepositoryPort;
import com.cuenta_bancaria.user.infra.data.entity.UserEntity;
import com.cuenta_bancaria.user.infra.data.mapper.UserMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class UserJpaAdapter implements UserRepositoryPort {

    private final UserJpaRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public User save(User user) {
        UserEntity entity = userMapper.toEntity(user);
        UserEntity saved = userRepository.save(entity);
        return userMapper.toDomain(saved);
    }

    @Override
    public Optional<User> findById(Long id) {
        return userRepository.findById(id)
                .map(userMapper::toDomain);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        if (email.isEmpty() || email.trim().isBlank()) {
            throw new IllegalArgumentException("El email no puedo estar vacio");
        }
        return userRepository.findByEmail(email)
                .map(userMapper::toDomain);
    }

    @Override
    public Optional<User> findByAlias(String alias) {
        return userRepository.findByAlias(alias)
                .map(userMapper::toDomain);
    }

    @Override
    public List<User> findAll() {
        return userRepository.findAll()
                .stream()
                .map(userMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public boolean existsById(Long id) {
        return userRepository.existsById(id);
    }

    @Override
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
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
