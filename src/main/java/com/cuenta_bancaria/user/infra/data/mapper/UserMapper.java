package com.cuenta_bancaria.user.infra.data.mapper;

import com.cuenta_bancaria.user.domain.User;
import com.cuenta_bancaria.user.infra.data.entity.UserEntity;

public class UserMapper {

    public static UserEntity toEntity(User user) {
        return UserEntity.builder()
                .id(user.getId())
                .firstname(user.getFirstname())
                .lastname(user.getLastname())
                .alias(user.getAlias())
                .email(user.getEmail())
                .password(user.getPassword())
                .build();
    }

    public static User toDomain(UserEntity user) {
        return User.builder()
                .id(user.getId())
                .firstname(user.getFirstname())
                .lastname(user.getLastname())
                .alias(user.getAlias())
                .email(user.getEmail())
                .password(user.getPassword())
                .build();
    }

}
