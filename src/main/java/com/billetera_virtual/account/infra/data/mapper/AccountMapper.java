package com.billetera_virtual.account.infra.data.mapper;

import com.billetera_virtual.account.domain.Account;
import com.billetera_virtual.account.infra.data.entity.AccountEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AccountMapper {

    @Mapping(source = "user_id", target = "userId")
    AccountEntity toEntity(Account domain);

    @Mapping(source = "userId", target = "user_id")
    Account toDomain(AccountEntity entity);
}
