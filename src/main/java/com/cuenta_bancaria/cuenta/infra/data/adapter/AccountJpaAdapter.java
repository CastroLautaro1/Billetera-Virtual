package com.cuenta_bancaria.cuenta.infra.data.adapter;

import com.cuenta_bancaria.cuenta.domain.Account;
import com.cuenta_bancaria.cuenta.domain.port.AccountRepositoryPort;
import com.cuenta_bancaria.cuenta.domain.port.CvuGeneratorPort;
import com.cuenta_bancaria.cuenta.infra.data.entity.AccountEntity;
import com.cuenta_bancaria.cuenta.infra.data.mapper.AccountMapper;
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
                .status(true)
                .build();

        entity = jpaRepository.save(entity);

        String cvu = cvuGenerator.generate(entity.getId());
        entity.setCvu(cvu);

        AccountEntity savedEntity =  jpaRepository.saveAndFlush(entity);

        return accountMapper.toDomain(savedEntity);
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
}
