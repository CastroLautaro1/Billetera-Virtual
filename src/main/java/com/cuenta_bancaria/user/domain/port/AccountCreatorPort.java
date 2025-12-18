package com.cuenta_bancaria.user.domain.port;

public interface AccountCreatorPort {

    // Utilizo este puerto para poder comunicarme con el AccountService
    void createInitialAccount(Long userId);

}
