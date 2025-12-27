package com.cuenta_bancaria.auth.domain;

public interface CreateAccountExternalPort {

    void createAccountByUserId(Long userId);
}
