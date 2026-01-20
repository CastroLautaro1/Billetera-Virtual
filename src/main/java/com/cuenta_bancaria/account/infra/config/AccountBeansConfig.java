package com.cuenta_bancaria.account.infra.config;

import com.cuenta_bancaria.account.application.AccountService;
import com.cuenta_bancaria.account.domain.port.AccountRepositoryPort;
import com.cuenta_bancaria.account.domain.port.AccountServicePort;
import com.cuenta_bancaria.account.domain.port.AliasGeneratorPort;
import com.cuenta_bancaria.account.domain.port.CvuGeneratorPort;
import com.cuenta_bancaria.account.infra.data.adapter.AccountJpaAdapter;
import com.cuenta_bancaria.account.infra.data.adapter.AccountJpaRepository;
import com.cuenta_bancaria.account.infra.data.mapper.AccountMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AccountBeansConfig {

    @Bean
    public AccountRepositoryPort accountRepositoryPort(AccountJpaRepository jpaRepository,
                                                       AccountMapper accountMapper,
                                                       CvuGeneratorPort cvuGeneratorPort,
                                                       AliasGeneratorPort aliasGeneratorPort) {
        return new AccountJpaAdapter(jpaRepository, accountMapper, cvuGeneratorPort, aliasGeneratorPort);
    }

    @Bean
    public AccountServicePort accountServicePort(AccountRepositoryPort repositoryPort) {
        return new AccountService(repositoryPort);
    }
}
