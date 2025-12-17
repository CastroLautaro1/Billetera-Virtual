package com.cuenta_bancaria.cuenta.infra.web.mapper;

import com.cuenta_bancaria.cuenta.domain.Account;
import com.cuenta_bancaria.cuenta.infra.web.dto.AccountRequest;

public class AccountMapperWeb {

    public static Account toDomain(AccountRequest request) {
        return Account.builder()
                .user_id(request.getIdUser())
                .balance(request.getAmount())
                .build();

    }

}
