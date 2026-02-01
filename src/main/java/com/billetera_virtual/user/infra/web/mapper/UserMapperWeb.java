package com.billetera_virtual.user.infra.web.mapper;

import com.billetera_virtual.user.domain.User;
import com.billetera_virtual.user.infra.web.dto.CreateUserRequest;
import com.billetera_virtual.user.infra.web.dto.UserDataResponse;
import com.billetera_virtual.user.infra.web.dto.UserProfileResponse;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(
    componentModel = "spring",
    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface UserMapperWeb {

    User toDomain(CreateUserRequest request);

    UserDataResponse toResponse(User user);

    UserProfileResponse toProfileResponse(User user);
}
