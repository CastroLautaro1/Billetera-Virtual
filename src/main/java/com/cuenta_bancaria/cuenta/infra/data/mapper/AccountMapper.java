package com.cuenta_bancaria.cuenta.infra.data.mapper;

import com.cuenta_bancaria.cuenta.domain.Account;
import com.cuenta_bancaria.cuenta.infra.data.entity.AccountEntity;

public class AccountMapper {

    public static AccountEntity toEntity(Account domain) {
        return AccountEntity.builder()
                .id(domain.getId())
                .idUser(domain.getUser_id())
                .amount(domain.getBalance())
                .status(domain.isStatus())
                .build();
    }


    public static Account toDomain(AccountEntity entity) {
        return Account.builder()
                .id(entity.getId())
                .user_id(entity.getUser_id())
                .balance(entity.getBalance())
                .status(entity.isStatus())
                .build();
    }

}
