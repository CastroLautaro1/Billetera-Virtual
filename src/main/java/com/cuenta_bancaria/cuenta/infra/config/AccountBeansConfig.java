package com.cuenta_bancaria.cuenta.infra.config;

import com.cuenta_bancaria.cuenta.application.AccountService;
import com.cuenta_bancaria.cuenta.domain.port.AccountRepositoryPort;
import com.cuenta_bancaria.cuenta.domain.port.AccountServicePort;
import com.cuenta_bancaria.cuenta.infra.data.adapter.AccountJpaAdapter;
import com.cuenta_bancaria.cuenta.infra.data.adapter.AccountJpaRepository;
import com.cuenta_bancaria.cuenta.infra.data.mapper.AccountMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AccountBeansConfig {

    @Bean
    public AccountRepositoryPort accountRepositoryPort(AccountJpaRepository jpaRepository, AccountMapper accountMapper) {
        return new AccountJpaAdapter(jpaRepository, accountMapper);
    }

    @Bean
    public AccountServicePort accountServicePort(AccountRepositoryPort repositoryPort) {
        return new AccountService(repositoryPort);
    }
}
