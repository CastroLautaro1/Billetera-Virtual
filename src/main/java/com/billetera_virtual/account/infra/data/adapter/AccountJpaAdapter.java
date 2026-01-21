package com.billetera_virtual.account.infra.data.adapter;

import com.billetera_virtual.account.domain.Account;
import com.billetera_virtual.account.domain.port.AccountRepositoryPort;
import com.billetera_virtual.account.domain.port.external.AliasGeneratorPort;
import com.billetera_virtual.account.domain.port.external.CvuGeneratorPort;
import com.billetera_virtual.account.infra.data.entity.AccountEntity;
import com.billetera_virtual.account.infra.data.mapper.AccountMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class AccountJpaAdapter implements AccountRepositoryPort {

    private final AccountJpaRepository jpaRepository;
    private final AccountMapper accountMapper;
    private final CvuGeneratorPort cvuGenerator;
    private final AliasGeneratorPort aliasGenerator;

    @Override
    public Account save(Account account) {
        AccountEntity entity = accountMapper.toEntity(account);
        AccountEntity savedEntity = jpaRepository.save(entity);
        return accountMapper.toDomain(savedEntity);
    }

    @Override
    public Account createAccountFromUser(Long idUser) {
        // Recibo el Id del Usuario y creo la cuenta correspondiente
        AccountEntity entity = AccountEntity.builder()
                .userId(idUser)
                .balance(0)
                .alias(generateAlias()) // Se genera el alias
                .status(true)
                .build();

        entity = jpaRepository.save(entity);

        String cvu = cvuGenerator.generate(entity.getId()); // Con el ID de la cuenta se crea el CVU
        entity.setCvu(cvu);

        AccountEntity savedEntity = jpaRepository.saveAndFlush(entity);

        return accountMapper.toDomain(savedEntity);
    }

    public String generateAlias() {
        String alias;
        do {
            alias = aliasGenerator.generate();
        } while (existsByAlias(alias));
        return alias;
    }

    @Override
    public Optional<Account> getById(Long id) {
        return jpaRepository.findById(id)
                .map(accountMapper::toDomain);
    }

    @Override
    public Optional<Account> getByIdUser(Long userId) {
        return jpaRepository.findByUserId(userId)
                .map(accountMapper::toDomain);
    }

    @Override
    public Optional<Account> getAccountByAlias(String alias) {
        return jpaRepository.findByAlias(alias)
                .map(accountMapper::toDomain);
    }

    @Override
    public Optional<Account> getAccountByCvu(String cvu) {
        return jpaRepository.findByCvu(cvu)
                .map(accountMapper::toDomain);
    }

    @Override
    public List<Account> findAll() {
        List<AccountEntity> entities = jpaRepository.findAll();

        return entities.stream()
                        .map(accountMapper::toDomain)
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

    @Override
    public boolean existsByAlias(String alias) {
        return jpaRepository.existsByAlias(alias);
    }
}
