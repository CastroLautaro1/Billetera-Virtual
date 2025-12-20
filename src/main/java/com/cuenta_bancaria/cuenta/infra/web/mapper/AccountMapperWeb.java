package com.cuenta_bancaria.cuenta.infra.web.mapper;

import com.cuenta_bancaria.cuenta.domain.Account;
import com.cuenta_bancaria.cuenta.infra.web.dto.AccountRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(
    componentModel = "spring",
    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface AccountMapperWeb {

    @Mapping(source = "idUser", target = "user_id")
    Account toDomain(AccountRequest request);

}
