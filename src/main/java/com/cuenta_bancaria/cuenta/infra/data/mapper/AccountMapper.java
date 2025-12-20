package com.cuenta_bancaria.cuenta.infra.data.mapper;

import com.cuenta_bancaria.cuenta.domain.Account;
import com.cuenta_bancaria.cuenta.infra.data.entity.AccountEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface AccountMapper {

    @Mapping(source = "user_id", target = "userId")
    AccountEntity toEntity(Account domain);

    @Mapping(source = "userId", target = "user_id")
    Account toDomain(AccountEntity entity);
}
