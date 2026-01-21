package com.billetera_virtual.account.infra.config;

import com.billetera_virtual.account.application.AccountService;
import com.billetera_virtual.account.domain.port.AccountRepositoryPort;
import com.billetera_virtual.account.domain.port.AccountServicePort;
import com.billetera_virtual.account.domain.port.external.AliasGeneratorPort;
import com.billetera_virtual.account.domain.port.external.CvuGeneratorPort;
import com.billetera_virtual.account.infra.data.adapter.AccountJpaAdapter;
import com.billetera_virtual.account.infra.data.adapter.AccountJpaRepository;
import com.billetera_virtual.account.infra.data.mapper.AccountMapper;
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
