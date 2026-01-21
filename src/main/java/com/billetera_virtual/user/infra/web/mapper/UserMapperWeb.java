package com.billetera_virtual.user.infra.web.mapper;

import com.billetera_virtual.user.domain.User;
import com.billetera_virtual.user.infra.web.dto.CreateUserRequest;
import com.billetera_virtual.user.infra.web.dto.UserProfileResponse;
import com.billetera_virtual.user.infra.web.dto.UserResponse;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(
    componentModel = "spring",
    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface UserMapperWeb {

    User toDomain(CreateUserRequest request);

    UserResponse toResponse(User user);

    UserProfileResponse toProfileResponse(User user);
}
