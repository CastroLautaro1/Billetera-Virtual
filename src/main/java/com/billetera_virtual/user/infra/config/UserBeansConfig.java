package com.billetera_virtual.user.infra.config;

import com.billetera_virtual.account.domain.port.AccountServicePort;
import com.billetera_virtual.user.application.UserService;
import com.billetera_virtual.user.domain.port.UserServicePort;
import com.billetera_virtual.user.domain.port.external.AccountCreatorPort;
import com.billetera_virtual.user.domain.port.UserRepositoryPort;
import com.billetera_virtual.user.domain.port.external.PasswordEncoderPort;
import com.billetera_virtual.user.infra.data.adapters.UserJpaAdapter;
import com.billetera_virtual.user.infra.data.adapters.UserJpaRepository;
import com.billetera_virtual.user.infra.data.external.UserAccountAdapter;
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
    public AccountCreatorPort accountCreatorPort(AccountServicePort accountService) {
        return new UserAccountAdapter(accountService);
    }

    @Bean
    public UserServicePort userServicePort(UserRepositoryPort userRepositoryPort, AccountCreatorPort accountCreatorPort, PasswordEncoderPort passwordEncoderPort) {
        return new UserService(userRepositoryPort, accountCreatorPort, passwordEncoderPort);
    }

}
