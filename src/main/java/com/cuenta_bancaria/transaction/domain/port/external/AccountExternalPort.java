package com.cuenta_bancaria.transaction.domain.port.external;

public interface AccountExternalPort {

    // Con esos parametros el modulo Account se puede encargar de realizar la transaccion
    double makeTransaction(Long originId, Long counterpartyId, double amount);

    // Obtengo el ID de la cuenta para guardarlo en la transaccion
    Long getAccountIdByUserId(Long userId);

}
