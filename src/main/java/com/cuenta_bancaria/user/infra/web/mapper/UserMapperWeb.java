package com.cuenta_bancaria.user.infra.web.mapper;

import com.cuenta_bancaria.user.domain.User;
import com.cuenta_bancaria.user.infra.web.dto.CreateUserRequest;

public class UserMapperWeb {

    public static User toDomain(CreateUserRequest request) {
        return User.builder()
                .firstname(request.getFirstname())
                .lastname(request.getLastname())
                .alias(request.getAlias())
                .email(request.getEmail())
                .password(request.getPassword())
                .build();
    }

}
