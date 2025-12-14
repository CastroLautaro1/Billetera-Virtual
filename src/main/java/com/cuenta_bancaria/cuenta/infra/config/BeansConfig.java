package com.cuenta_bancaria.cuenta.infra.config;

import com.cuenta_bancaria.cuenta.application.GestionCuentaService;
import com.cuenta_bancaria.cuenta.domain.port.CuentaRepositoryPort;
import com.cuenta_bancaria.cuenta.infra.data.adapter.CuentaJpaAdapter;
import com.cuenta_bancaria.cuenta.infra.data.adapter.CuentaJpaRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeansConfig {

    @Bean
    public CuentaRepositoryPort cuentaRepositoryPort(CuentaJpaRepository jpaRepository) {
        return new CuentaJpaAdapter(jpaRepository);
    }

    @Bean
    public GestionCuentaService gestionCuentaService(CuentaRepositoryPort repositoryPort) {
        return new GestionCuentaService(repositoryPort);
    }
}
