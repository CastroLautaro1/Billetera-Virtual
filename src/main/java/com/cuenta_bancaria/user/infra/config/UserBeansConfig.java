package com.cuenta_bancaria.user.infra.config;

import com.cuenta_bancaria.user.application.UserService;
import com.cuenta_bancaria.user.domain.port.UserRepositoryPort;
import com.cuenta_bancaria.user.infra.data.adapters.UserJpaAdapter;
import com.cuenta_bancaria.user.infra.data.adapters.UserJpaRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UserBeansConfig {

    @Bean
    public UserRepositoryPort userRepositoryPort(UserJpaRepository userJpaRepository) {
        return new UserJpaAdapter(userJpaRepository);
    }

    @Bean
    public UserService userService(UserRepositoryPort userRepositoryPort) {
        return new UserService(userRepositoryPort);
    }

}
