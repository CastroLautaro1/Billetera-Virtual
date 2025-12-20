package com.cuenta_bancaria.user.infra.web.mapper;

import com.cuenta_bancaria.user.domain.User;
import com.cuenta_bancaria.user.infra.web.dto.CreateUserRequest;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(
    componentModel = "spring",
    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface UserMapperWeb {

    User toDomain(CreateUserRequest request);
}
