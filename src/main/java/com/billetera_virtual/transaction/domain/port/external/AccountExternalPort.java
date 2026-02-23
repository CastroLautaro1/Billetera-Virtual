package com.billetera_virtual.transaction.domain.port.external;

import java.math.BigDecimal;

public interface AccountExternalPort {

    // Con esos parametros el modulo Account se puede encargar de realizar la transaccion
    BigDecimal makeTransaction(Long originId, Long counterpartyId, BigDecimal amount);

    // Obtengo el ID de la cuenta para guardarlo en la transaccion
    Long getAccountIdByUserId(Long userId);

    // Obtengo el ID de la contraparte usando el alias o CVU
    Long getCounterpartyAccountIdByDestination(String destination);

}
