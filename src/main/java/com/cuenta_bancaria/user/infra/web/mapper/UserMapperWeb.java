package com.cuenta_bancaria.user.infra.web.mapper;

import com.cuenta_bancaria.user.domain.User;
import com.cuenta_bancaria.user.infra.web.dto.CreateUserRequest;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapperWeb {

    User toDomain(CreateUserRequest request);
}
