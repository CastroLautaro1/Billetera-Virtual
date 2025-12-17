package com.cuenta_bancaria.cuenta.infra.data.adapter;

import com.cuenta_bancaria.cuenta.domain.Account;
import com.cuenta_bancaria.cuenta.domain.port.AccountRepositoryPort;
import com.cuenta_bancaria.cuenta.infra.data.entity.AccountEntity;
import com.cuenta_bancaria.cuenta.infra.data.mapper.AccountMapper;
import jakarta.transaction.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class AccountJpaAdapter implements AccountRepositoryPort {

    private AccountJpaRepository jpaRepository;

    public AccountJpaAdapter(AccountJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Account save(Account account) {
        AccountEntity entity = AccountMapper.toEntity(account);
        AccountEntity savedEntity = jpaRepository.save(entity);
        return AccountMapper.toDomain(savedEntity);
    }

    @Override
    public Optional<Account> getById(Long id) {
        return jpaRepository.findById(id)
                .map(AccountMapper::toDomain);
    }

    @Override
    public Optional<Account> getByIdUser(Long userId) {
        return jpaRepository.findByUserId(userId)
                .map(AccountMapper::toDomain);
    }

    @Override
    public List<Account> findAll() {
        List<AccountEntity> entities = jpaRepository.findAll();

        return entities.stream()
                        .map(AccountMapper::toDomain)
                        .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void logicallyDeleteById(Long id) {
        jpaRepository.logicallyDeleteById(id);
    }

    @Override
    public boolean existsById(Long id) {
        return jpaRepository.existsById(id);
    }

    @Override
    public boolean existsByIdUser(Long userId) {
        return jpaRepository.existsByUserId(userId);
    }
}
