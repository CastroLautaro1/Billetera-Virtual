package com.cuenta_bancaria.cuenta.infra.config;

import com.cuenta_bancaria.cuenta.application.AccountService;
import com.cuenta_bancaria.cuenta.domain.port.AccountRepositoryPort;
import com.cuenta_bancaria.cuenta.infra.data.adapter.AccountJpaAdapter;
import com.cuenta_bancaria.cuenta.infra.data.adapter.AccountJpaRepository;
import com.cuenta_bancaria.cuenta.infra.data.mapper.AccountMapper;
import com.cuenta_bancaria.cuenta.infra.web.mapper.AccountMapperWeb;
import org.mapstruct.factory.Mappers;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AccountBeansConfig {

    @Bean
    public AccountRepositoryPort cuentaRepositoryPort(AccountJpaRepository jpaRepository, AccountMapper accountMapper) {
        return new AccountJpaAdapter(jpaRepository, accountMapper);
    }

    // Aca deberia ser AccountServicePort, o sea va el puerto en lugar de la implementación
    @Bean
    public AccountService gestionCuentaService(AccountRepositoryPort repositoryPort) {
        return new AccountService(repositoryPort);
    }
}
