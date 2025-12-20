package com.cuenta_bancaria.transaction.infra.data.external;

import com.cuenta_bancaria.transaction.domain.port.external.UserExternalPort;
import com.cuenta_bancaria.user.domain.port.UserServicePort;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class UserExternalAdapter implements UserExternalPort {

    private final UserServicePort userService;

    @Override
    public Long getUserIdByAlias(String alias) {
        return userService.getByAlias(alias).getId();
    }
}
