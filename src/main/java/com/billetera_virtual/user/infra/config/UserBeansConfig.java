package com.billetera_virtual.user.infra.config;

import com.billetera_virtual.account.domain.port.AccountServicePort;
import com.billetera_virtual.user.application.UserService;
import com.billetera_virtual.user.domain.port.UserServicePort;
import com.billetera_virtual.user.domain.port.UserRepositoryPort;
import com.billetera_virtual.user.domain.port.external.AccountDataExternalPort;
import com.billetera_virtual.user.domain.port.external.PasswordEncoderPort;
import com.billetera_virtual.user.infra.data.adapters.UserJpaAdapter;
import com.billetera_virtual.user.infra.data.adapters.UserJpaRepository;
import com.billetera_virtual.user.infra.data.external.AccountDataAdapter;
import com.billetera_virtual.user.infra.data.mapper.UserMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UserBeansConfig {

    @Bean
    public UserRepositoryPort userRepositoryPort(UserJpaRepository userJpaRepository, UserMapper userMapper) {
        return new UserJpaAdapter(userJpaRepository, userMapper);
    }

    @Bean
    public AccountDataExternalPort accountDataExternalPort(AccountServicePort accountServicePort) {
        return new AccountDataAdapter(accountServicePort);
    }

    @Bean
    public UserServicePort userServicePort(UserRepositoryPort userRepositoryPort,
                                           PasswordEncoderPort passwordEncoderPort,
                                           AccountDataExternalPort accountDataExternalPort) {
        return new UserService(userRepositoryPort, passwordEncoderPort, accountDataExternalPort);
    }

}
