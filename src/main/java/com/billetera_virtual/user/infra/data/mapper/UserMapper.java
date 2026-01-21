package com.billetera_virtual.user.infra.data.mapper;

import com.billetera_virtual.user.domain.User;
import com.billetera_virtual.user.infra.data.entity.UserEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {

    User toDomain(UserEntity entity);

    UserEntity toEntity(User domain);
}
