package com.billetera_virtual.transaction.infra.config;

import com.billetera_virtual.account.domain.port.AccountServicePort;
import com.billetera_virtual.transaction.application.TransactionService;
import com.billetera_virtual.transaction.domain.port.TransactionRepositoryPort;
import com.billetera_virtual.transaction.domain.port.TransactionServicePort;
import com.billetera_virtual.transaction.domain.port.external.AccountExternalPort;
import com.billetera_virtual.transaction.domain.port.external.ReceiptGeneratorPort;
import com.billetera_virtual.transaction.infra.data.adapter.TransactionJpaAdapter;
import com.billetera_virtual.transaction.infra.data.adapter.TransactionJpaRepository;
import com.billetera_virtual.transaction.infra.data.external.AccountExternalAdapter;
import com.billetera_virtual.transaction.infra.data.external.ReceiptGeneratorAdapter;
import com.billetera_virtual.transaction.infra.data.mapper.TransactionMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.thymeleaf.TemplateEngine;

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
            TemplateEngine templateEngine)
    {
        AccountExternalPort accountExternal = new AccountExternalAdapter(accountService);
        ReceiptGeneratorPort receiptExternal = new ReceiptGeneratorAdapter(templateEngine);

        return new TransactionService(transactionRepositoryPort, accountExternal, receiptExternal);
    }

}
