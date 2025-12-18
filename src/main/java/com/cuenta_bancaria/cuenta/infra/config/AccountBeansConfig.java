package com.cuenta_bancaria.cuenta.infra.config;

import com.cuenta_bancaria.cuenta.application.AccountService;
import com.cuenta_bancaria.cuenta.domain.port.AccountRepositoryPort;
import com.cuenta_bancaria.cuenta.infra.data.adapter.AccountJpaAdapter;
import com.cuenta_bancaria.cuenta.infra.data.adapter.AccountJpaRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AccountBeansConfig {

    @Bean
    public AccountRepositoryPort cuentaRepositoryPort(AccountJpaRepository jpaRepository) {
        return new AccountJpaAdapter(jpaRepository);
    }

    @Bean
    public AccountService gestionCuentaService(AccountRepositoryPort repositoryPort) {
        return new AccountService(repositoryPort);
    }
}
