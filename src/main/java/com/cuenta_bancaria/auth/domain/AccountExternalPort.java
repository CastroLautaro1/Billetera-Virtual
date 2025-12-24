package com.cuenta_bancaria.auth.domain;

public interface AccountExternalPort {

    void createAccountByUserId(Long userId);
}
