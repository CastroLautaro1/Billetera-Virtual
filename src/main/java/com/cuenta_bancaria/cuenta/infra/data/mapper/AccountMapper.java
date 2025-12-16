package com.cuenta_bancaria.cuenta.infra.data.mapper;

import com.cuenta_bancaria.cuenta.domain.Account;
import com.cuenta_bancaria.cuenta.infra.data.entity.AccountEntity;

public class AccountMapper {

    public static AccountEntity toEntity(Account domain) {
        return AccountEntity.builder()
                .id(domain.getId())
                .idUser(domain.getIdUser())
                .amount(domain.getAmount())
                .status(domain.isStatus())
                .build();
    }


    public static Account toDomain(AccountEntity entity) {
        return Account.builder()
                .id(entity.getId())
                .idUser(entity.getIdUser())
                .amount(entity.getAmount())
                .status(entity.isStatus())
                .build();
    }

}
