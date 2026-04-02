package com.billetera_virtual.account.infra.config.batch;

import com.billetera_virtual.account.infra.data.adapter.AccountJpaRepository;
import com.billetera_virtual.account.infra.data.entity.AccountEntity;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.data.RepositoryItemReader;
import org.springframework.batch.item.data.RepositoryItemWriter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.PlatformTransactionManager;

import java.math.BigDecimal;
import java.util.Collections;

@Configuration
public class InterestBatchConfig {

    // 1. EL LECTOR: Trae las cuentas de a 100
    @Bean
    public RepositoryItemReader<AccountEntity> accountItemReader(AccountJpaRepository repository) {
        RepositoryItemReader<AccountEntity> reader = new RepositoryItemReader<>();
        reader.setRepository(repository);
        // Suponiendo que tenés un método en tu repositorio para traer todas las cuentas
        // Si no lo tenés, podés crear uno simple como List<AccountEntity> findAll()
        reader.setMethodName("findAll");
        reader.setSort(Collections.singletonMap("id", Sort.Direction.ASC));
        reader.setPageSize(100);
        return reader;
    }

    // 2. EL PROCESADOR: Calcula el 0.5% de interés
    @Bean
    public ItemProcessor<AccountEntity, AccountEntity> accountItemProcessor() {
        return account -> {
            BigDecimal balance = account.getBalance();

            // Solo aplicamos interés si el saldo es mayor a 0
            if (balance.compareTo(BigDecimal.ZERO) > 0) {
                BigDecimal interest = balance.multiply(new BigDecimal("0.005"));
                account.setBalance(balance.add(interest));
                System.out.println("Interés de $" + interest + " aplicado a la cuenta ID: " + account.getId());
            }
            return account;
        };
    }

    // 3. EL ESCRITOR: Guarda las cuentas actualizadas masivamente
    @Bean
    public RepositoryItemWriter<AccountEntity> accountItemWriter(AccountJpaRepository repository) {
        RepositoryItemWriter<AccountEntity> writer = new RepositoryItemWriter<>();
        writer.setRepository(repository);
        writer.setMethodName("save");
        return writer;
    }

    // 4. EL PASO: Une lector, procesador y escritor
    @Bean
    public Step interestStep(JobRepository jobRepository,
                             PlatformTransactionManager transactionManager,
                             RepositoryItemReader<AccountEntity> reader,
                             ItemProcessor<AccountEntity, AccountEntity> processor,
                             RepositoryItemWriter<AccountEntity> writer) {

        return new StepBuilder("interestStep", jobRepository)
                .<AccountEntity, AccountEntity>chunk(100, transactionManager)
                .reader(reader)
                .processor(processor)
                .writer(writer)
                .build();
    }

    // 5. EL TRABAJO: El contenedor final que llamaremos desde el Scheduler
    @Bean
    public Job payInterestJob(JobRepository jobRepository, Step interestStep) {
        return new JobBuilder("payInterestJob", jobRepository)
                .start(interestStep)
                .build();
    }

}
