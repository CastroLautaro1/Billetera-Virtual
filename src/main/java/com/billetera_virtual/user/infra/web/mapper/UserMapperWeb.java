package com.billetera_virtual.user.infra.web.mapper;

import com.billetera_virtual.user.domain.User;
import com.billetera_virtual.user.infra.web.dto.UpdateUserRequest;
import com.billetera_virtual.user.infra.web.dto.UserFullnameResponse;
import com.billetera_virtual.user.infra.web.dto.UserProfileResponse;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(
    componentModel = "spring",
    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface UserMapperWeb {

    User toDomain(UpdateUserRequest request);

    UserFullnameResponse toResponse(User user);

    UserProfileResponse toProfileResponse(User user);
}
