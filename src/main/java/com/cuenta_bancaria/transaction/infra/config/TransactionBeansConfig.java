package com.cuenta_bancaria.transaction.infra.config;

import com.cuenta_bancaria.cuenta.domain.port.AccountServicePort;
import com.cuenta_bancaria.transaction.application.TransactionService;
import com.cuenta_bancaria.transaction.domain.port.TransactionRepositoryPort;
import com.cuenta_bancaria.transaction.domain.port.TransactionServicePort;
import com.cuenta_bancaria.transaction.domain.port.external.AccountExternalPort;
import com.cuenta_bancaria.transaction.domain.port.external.UserExternalPort;
import com.cuenta_bancaria.transaction.infra.data.adapter.TransactionJpaAdapter;
import com.cuenta_bancaria.transaction.infra.data.adapter.TransactionJpaRepository;
import com.cuenta_bancaria.transaction.infra.data.external.AccountExternalAdapter;
import com.cuenta_bancaria.transaction.infra.data.external.UserExternalAdapter;
import com.cuenta_bancaria.transaction.infra.data.mapper.TransactionMapper;
import com.cuenta_bancaria.user.domain.port.UserServicePort;
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
            AccountServicePort accountService,
            UserServicePort userService)
    {
        AccountExternalPort accountExternal = new AccountExternalAdapter(accountService);
        UserExternalPort userExternal = new UserExternalAdapter(userService);

        return new TransactionService(transactionRepositoryPort, accountExternal, userExternal);
    }

}
