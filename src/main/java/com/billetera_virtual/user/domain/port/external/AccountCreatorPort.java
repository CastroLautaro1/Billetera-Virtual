package com.billetera_virtual.user.domain.port.external;

public interface AccountCreatorPort {

    // Utilizo este puerto para poder comunicarme con el AccountService
    void createInitialAccount(Long userId);

}
