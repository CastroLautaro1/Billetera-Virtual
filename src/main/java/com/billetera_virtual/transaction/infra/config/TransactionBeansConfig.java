package com.billetera_virtual.transaction.infra.config;

import com.billetera_virtual.account.domain.port.AccountServicePort;
import com.billetera_virtual.transaction.application.TransactionService;
import com.billetera_virtual.transaction.domain.port.TransactionRepositoryPort;
import com.billetera_virtual.transaction.domain.port.TransactionServicePort;
import com.billetera_virtual.transaction.domain.port.external.AccountExternalPort;
import com.billetera_virtual.transaction.infra.data.adapter.TransactionJpaAdapter;
import com.billetera_virtual.transaction.infra.data.adapter.TransactionJpaRepository;
import com.billetera_virtual.transaction.infra.data.external.AccountExternalAdapter;
import com.billetera_virtual.transaction.infra.data.mapper.TransactionMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TransactionBeansConfig {


    @Bean
    public TransactionRepositoryPort transactionRepositoryPort(TransactionJpaRepository transactionJpaRepository, TransactionMapper transactionMapper) {
        return new TransactionJpaAdapter(transactionJpaRepository, transactionMapper);
    }

    @Bean
    public TransactionServicePort transactionServicePort(
            TransactionRepositoryPort transactionRepositoryPort,
            AccountServicePort accountService)
    {
        AccountExternalPort accountExternal = new AccountExternalAdapter(accountService);

        return new TransactionService(transactionRepositoryPort, accountExternal);
    }

}
