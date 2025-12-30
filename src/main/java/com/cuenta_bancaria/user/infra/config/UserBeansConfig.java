package com.cuenta_bancaria.user.infra.config;

import com.cuenta_bancaria.cuenta.domain.port.AccountServicePort;
import com.cuenta_bancaria.user.application.UserService;
import com.cuenta_bancaria.user.domain.port.UserServicePort;
import com.cuenta_bancaria.user.domain.port.external.AccountCreatorPort;
import com.cuenta_bancaria.user.domain.port.UserRepositoryPort;
import com.cuenta_bancaria.user.domain.port.external.PasswordEncoderPort;
import com.cuenta_bancaria.user.infra.data.adapters.UserJpaAdapter;
import com.cuenta_bancaria.user.infra.data.adapters.UserJpaRepository;
import com.cuenta_bancaria.user.infra.data.external.UserAccountAdapter;
import com.cuenta_bancaria.user.infra.data.mapper.UserMapper;
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
