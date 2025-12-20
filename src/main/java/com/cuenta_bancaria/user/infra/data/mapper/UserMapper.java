package com.cuenta_bancaria.user.infra.data.mapper;

import com.cuenta_bancaria.user.domain.User;
import com.cuenta_bancaria.user.infra.data.entity.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface UserMapper {

    User toDomain(UserEntity entity);

    UserEntity toEntity(User domain);
}
