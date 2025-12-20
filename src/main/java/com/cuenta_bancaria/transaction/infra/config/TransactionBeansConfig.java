package com.cuenta_bancaria.transaction.infra.config;

import com.cuenta_bancaria.transaction.application.TransactionService;
import com.cuenta_bancaria.transaction.domain.port.TransactionRepositoryPort;
import com.cuenta_bancaria.transaction.domain.port.TransactionServicePort;
import com.cuenta_bancaria.transaction.infra.data.adapter.TransactionJpaAdapter;
import com.cuenta_bancaria.transaction.infra.data.adapter.TransactionJpaRepository;
import com.cuenta_bancaria.transaction.infra.data.mapper.TransactionMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TransactionBeansConfig {

    @Bean
    public TransactionRepositoryPort transactionRepositoryPort(TransactionJpaRepository transactionJpaRepository, TransactionMapper transactionMapper) {
        return new TransactionJpaAdapter(transactionJpaRepository, transactionMapper);
    }

    @Bean
    public TransactionServicePort transactionServicePort(TransactionRepositoryPort transactionRepositoryPort) {
        return new TransactionService(transactionRepositoryPort);
    }

}
